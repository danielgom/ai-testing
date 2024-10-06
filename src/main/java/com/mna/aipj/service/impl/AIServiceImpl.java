package com.mna.aipj.service.impl;

import com.mna.aipj.aidto.AnalyzerResponse;
import com.mna.aipj.aidto.AnalyzerResultResponse;
import com.mna.aipj.aidto.ClassifierResponse;
import com.mna.aipj.aidto.ClassifierResultResponse;
import com.mna.aipj.aimodel.Analyzer;
import com.mna.aipj.aimodel.Classifier;
import com.mna.aipj.dto.MentionInformation;
import com.mna.aipj.dto.TriggerUpdateResponse;
import com.mna.aipj.external.brandwatch.BrandWatchMention;
import com.mna.aipj.external.brandwatch.Brandwatch;
import com.mna.aipj.mapper.MentionMapper;
import com.mna.aipj.model.Query;
import com.mna.aipj.repository.QueryRepository;
import com.mna.aipj.service.AIService;
import com.mna.aipj.service.SaverService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class AIServiceImpl implements AIService {

    private final Brandwatch brandwatch;

    private final ChatLanguageModel chatLanguageModel;

    private final QueryRepository queryRepository;

    private final SaverService saverService;

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    @Override
    public TriggerUpdateResponse triggerUpdate(String queryID) {
        List<BrandWatchMention> mentions;

        Optional<Query> optionalQuery = queryRepository.findByQueryID(queryID);
        if (optionalQuery.isPresent()) {
            Query query = optionalQuery.get();
            mentions = brandwatch.getQueryMentionsAddedSince(queryID, query.getLastMentionDate())
                    .getResults();
            saverService.saveUpdateQuery(queryID, lastMentionDateFromMentions(mentions));
        } else {
            mentions = brandwatch.getInitialQueryMentions(queryID).getResults();
            saverService.saveUpdateQuery(queryID, lastMentionDateFromMentions(mentions));
        }

        if (mentions.isEmpty()) {
            logger.info("no mentions retrieved from Brandwatch API");
            return TriggerUpdateResponse.builder()
                    .completed(true)
                    .completedAt(LocalDateTime.now())
                    .build();
        }

        // To mention metadataInfo
        List<MentionInformation> mentionInformationList = this.toMentionInformationList(mentions);

        // Mention classification important/non-important
        List<MentionInformation> classifiedMentions = this.classifyMentions(mentionInformationList);

        logger.info("separating mentions by importance...");
        Map<Boolean, List<MentionInformation>> mentionGroups = classifiedMentions
                .stream()
                .collect(Collectors.groupingBy(MentionInformation::isImportant));

        List<MentionInformation> importantMentionList = mentionGroups
                .getOrDefault(true, List.of());

        List<MentionInformation> unimportantMentionList = mentionGroups
                .getOrDefault(false, List.of());

        if (!unimportantMentionList.isEmpty()) {
            logger.info("saving ({}) unimportant mentions...", unimportantMentionList.size());
            saverService.saveUnimportantMentions(unimportantMentionList);
        } else {
            logger.info("there are no unimportant mentions to save");
        }

        if (importantMentionList.isEmpty()) {
            logger.info("there are no mentions to analise");
            return TriggerUpdateResponse.builder()
                    .gotFromAPI(mentions.size())
                    .totalClassified(classifiedMentions.size())
                    .completed(true)
                    .completedAt(LocalDateTime.now())
                    .build();
        }

        logger.info("analysing ({}) mentions...", importantMentionList.size());

        // Mention analysis
        List<MentionInformation> analyzedMentions = this.analyzeMentions(importantMentionList);

        logger.info("saving ({}) important mentions...", analyzedMentions.size());
        saverService.saveAnalisedMentions(analyzedMentions);

        /*
        TODO: Save into Tables
             * Think about mention_trends if needed or not (most probably yes)
         */
        return TriggerUpdateResponse.builder()
                .gotFromAPI(mentions.size())
                .totalClassified(classifiedMentions.size())
                .totalAnalysed(analyzedMentions.size())
                .completed(true)
                .completedAt(LocalDateTime.now())
                .build();
    }

    private List<MentionInformation> analyzeMentions(List<MentionInformation> mentions) {
        logger.info("analyzing ({}) mentions...", mentions.size());
        String analizeEntry = IntStream.range(0, mentions.size())
                .mapToObj(idx -> {
                    MentionInformation mentionInformation = mentions.get(idx);
                    return String.format("-- %d -- -> -- %s -- -> -- %s --\n", idx,
                            mentionInformation.getTitle(), mentionInformation.getSnippet());
                })
                .collect(Collectors.joining());

        Analyzer analyzer = AiServices.create(Analyzer.class, chatLanguageModel);
        AnalyzerResponse analyzerResponse = analyzer.analyzeMentions(analizeEntry);

        List<AnalyzerResultResponse> analyzerResultList = analyzerResponse.getResponseList();

        IntStream.range(0, mentions.size())
                .forEach(idx -> {
                    MentionInformation mention = mentions.get(idx);
                    AnalyzerResultResponse mentionAnalysis = analyzerResultList.get(idx);

                    mention.setAnalyzed(true);
                    mention.setSentiment(mentionAnalysis.getSentiment());
                    mention.setTopic(mentionAnalysis.getTopic());
                    mention.setAlertLevel(mentionAnalysis.getAlertLevel());
                    mention.setActor(mentionAnalysis.getActor());
                    mention.setActorProfession(mentionAnalysis.getProfession());
                });

        logger.info("finished analyzing ({}) mentions...", mentions.size());
        return mentions;
    }

    private List<MentionInformation> classifyMentions(List<MentionInformation> mentions) {
        logger.info("classifying ({}) mentions...", mentions.size());
        String classifyEntry = IntStream.range(0, mentions.size())
                .mapToObj(idx -> {
                    MentionInformation mentionInformation = mentions.get(idx);
                    return String.format("-- %d -- -> -- %s -- -> -- %s --\n", idx,
                            mentionInformation.getTitle(), mentionInformation.getSnippet());
                })
                .collect(Collectors.joining());

        Classifier classifier = AiServices.create(Classifier.class, chatLanguageModel);
        ClassifierResponse classifierResponse = classifier.classifyMentions(classifyEntry);

        List<ClassifierResultResponse> classifierResultList = classifierResponse.getResponseList();

        IntStream.range(0, mentions.size())
                .forEach(idx -> {
                    MentionInformation mention = mentions.get(idx);
                    ClassifierResultResponse mentionClassification = classifierResultList.get(idx);

                    mention.setClassified(true);
                    mention.setImportant(mentionClassification.isImportant());
                });

        logger.info("finished classifying ({}) mentions...", mentions.size());
        return mentions;
    }

    private List<MentionInformation> toMentionInformationList(List<BrandWatchMention> mentions) {
        return mentions.stream()
                .filter(mention -> !mention.getContentSourceName().equals("X")) // Temp Skip (null values)
                .map(MentionMapper.MAPPER::toMentionInformation)
                .toList();
    }

    private LocalDateTime lastMentionDateFromMentions(List<BrandWatchMention> mentions) {
        return mentions.getFirst().getMentionDate();
    }
}
