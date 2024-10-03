package com.mna.aipj.service.impl;

import com.mna.aipj.aidto.ClassifierResponse;
import com.mna.aipj.aidto.ClassifierResultResponse;
import com.mna.aipj.aimodel.Classifier;
import com.mna.aipj.dto.TriggerUpdateResponse;
import com.mna.aipj.external.brandwatch.BrandWatchMention;
import com.mna.aipj.external.brandwatch.Brandwatch;
import com.mna.aipj.model.Query;
import com.mna.aipj.repository.QueryRepository;
import com.mna.aipj.service.AIService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class AIServiceImpl implements AIService {

    private final Brandwatch brandwatch;

    private final ChatLanguageModel chatLanguageModel;

    private final QueryRepository queryRepository;

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    @Override
    public TriggerUpdateResponse triggerUpdate(String queryID) {
        List<BrandWatchMention> mentions;

        Optional<Query> optionalQuery = queryRepository.findByQueryID(queryID);
        if (optionalQuery.isPresent()) {
            //mentions = brandwatch.getQueryMentionsAddedSince(queryID, query.getLastMentionDate()).getResults();
            //updateQueryTable(queryID, lastMentionDateFromMentions(mentions));
            mentions = List.of(); // Placeholder
            System.out.println("present");
        } else {
            mentions = brandwatch.getInitialQueryMentions(queryID).getResults();
            //updateQueryTable(queryID, lastMentionDateFromMentions(mentions));
        }

        logger.info("removing non-important mentions");
        List<BrandWatchMention> importantMentions = this.classifyMentions(mentions)
                .stream()
                .filter(BrandWatchMention::isImportant)
                .toList();


        /*
        TODO: Analise important mentions
             * Should return the actor found in the mention
             * Should return topic of the mention
             * Should return alertLevel

        TODO: Save into Tables
             * Save into query for the first time and consecutive times (remove this function's comments)
             * Read documentation for addedSince time format for non-first time following calls
             * Normalize actor if needed (perhaps let the AI decide this part)
             * Save into actor table
             * Save into mentions table (related to the actor)
             * Think about mention_trends if needed or not (most probably yes)
             * Save alert (If alert is NONE, means we just save the mention and the actor, no alert triggered)
         */
        return TriggerUpdateResponse.builder().build();
    }

    private List<BrandWatchMention> classifyMentions(List<BrandWatchMention> mentions) {
        logger.info("classifying mentions...");
        String classifyEntry = IntStream.range(0, mentions.size())
                .filter(idx -> !mentions.get(idx).getContentSourceName().equals("X")) // Temp Skip (null values)
                .mapToObj(idx -> {
                    BrandWatchMention currentMention = mentions.get(idx);
                    return String.format("-- %s -- -> -- %s --\n",
                            currentMention.getTitle(), currentMention.getSnippet());
                })
                .collect(Collectors.joining());

        Classifier classifier = AiServices.create(Classifier.class, chatLanguageModel);
        ClassifierResponse classifierResponse = classifier.classifyMentions(classifyEntry);

        List<ClassifierResultResponse> classifierResultList = classifierResponse.getResponseList();

        IntStream.range(0, mentions.size())
                .forEach(idx -> {
                    BrandWatchMention mention = mentions.get(idx);
                    ClassifierResultResponse mentionClassification = classifierResultList.get(idx);

                    mention.setClassified(true);
                    mention.setImportant(mentionClassification.isImportant());
                });

        logger.info("finished classifying mentions...");
        return mentions;
    }

    private LocalDateTime lastMentionDateFromMentions(List<BrandWatchMention> mentions) {
        return mentions.getFirst().getMentionDate();
    }

    private void updateQueryTable(String queryID, LocalDateTime lastMentionDate) {
        queryRepository.save(Query.builder()
                .queryID(queryID)
                .lastMentionDate(lastMentionDate)
                .build());
    }
}
