package com.mna.aipj.service.impl;

import com.mna.aipj.dto.MentionInformation;
import com.mna.aipj.model.*;
import com.mna.aipj.repository.*;
import com.mna.aipj.service.SaverService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaverServiceImpl implements SaverService {

    private final QueryRepository queryRepository;

    private final MentionRepository mentionRepository;

    private final ActorRepository actorRepository;

    private final AlertRepository alertRepository;

    private final MediaRankingRepository mediaRankingRepository;

    private final ActorMentionRepository actorMentionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SaverServiceImpl.class);

    private static final double DEFAULT_SCORE_OF_MISSING_SOURCE = 0.75;

    @Override
    public void saveQuery(String queryID, LocalDateTime lastMentiondate) {
        queryRepository.save(Query.builder()
                .queryID(queryID)
                .lastMentionDate(lastMentiondate)
                .build());
    }

    @Override
    public void updateQuery(String queryID, LocalDateTime lastMentionDate) {
        queryRepository.updateLastMentionDate(queryID, lastMentionDate);
    }

    @Override
    public void saveUnimportantMentions(List<MentionInformation> mentions) {
        LocalDateTime currentTime = LocalDateTime.now();
        int mentionsLength = mentions.size();
        logger.info("saving ({}) unimportant mentions...", mentionsLength);
        mentions.stream()
                .map(mentionInformation -> Mention.builder()
                        .platformSource(mentionInformation.getDomain())
                        .urlSource(mentionInformation.getUrl())
                        .type(mentionInformation.getSource())
                        .title(mentionInformation.getTitle())
                        .content(mentionInformation.getSnippet())
                        .mentionDate(mentionInformation.getDate())
                        .createdAt(currentTime)
                        .build())
                .forEach(mentionRepository::save);
        logger.info("finished saving ({}) unimportant mentions...", mentionsLength);
    }

    @Override
    public void saveAnalisedMentions(List<MentionInformation> mentions) {
        LocalDateTime currentTime = LocalDateTime.now();
        mentions.forEach(mentionInformation -> {
            String actorName = mentionInformation.getActor();
            String actorProfession = mentionInformation.getActorProfession();
            String mentionTopic = mentionInformation.getTopic();

            Actor actor;
            Optional<Actor> actorOpt = actorRepository.findByName(actorName);
            actor = actorOpt.orElseGet(() -> actorRepository.save(Actor.builder()
                    .name(actorName)
                    .occupation(actorProfession)
                    .createdAt(currentTime)
                    .build()));

            ActorMention actorMention = actorMentionRepository.save(ActorMention.builder()
                    .actor(actor)
                    .platformSource(mentionInformation.getDomain())
                    .urlSource(mentionInformation.getUrl())
                    .type(mentionInformation.getSource())
                    .title(mentionInformation.getTitle())
                    .sentiment(mentionInformation.getSentiment())
                    .content(mentionInformation.getSnippet())
                    .topic(mentionTopic)
                    .mentionDate(mentionInformation.getDate())
                    .createdAt(currentTime)
                    .build());

            ActorImportance actorImportance = mentionInformation.getActorImportance();
            TopicImportance topicImportance = mentionInformation.getTopicImportance();
            double sourceImportance = getSourceImportance(mentionInformation.getSource());

            alertRepository.save(Alert.builder()
                    .actorMention(actorMention)
                    .actorImportance(actorImportance)
                    .topicImportance(topicImportance)
                    .sourceImportance(sourceImportance)
                    .weighing(calculateWeighing(actorImportance,
                            topicImportance, sourceImportance))
                    .build());
        });
    }

    private double getSourceImportance(String urlSource) {
        final Optional<MediaRanking> domainOptional = mediaRankingRepository.findByDomain(urlSource);
        return domainOptional.map(MediaRanking::getScore).orElse(DEFAULT_SCORE_OF_MISSING_SOURCE);
    }

    private int calculateWeighing(ActorImportance actorImportance,
                                  TopicImportance topicImportance,
                                  double sourceImportance) {
        final int actorScore = switch (actorImportance) {
            case LOW -> 50;
            case MEDIUM -> 80;
            case HIGH -> 100;
            default -> 0;
        };

        final int topicScore = switch (topicImportance) {
            case LOW -> 25;
            case MEDIUM -> 75;
            case HIGH -> 100;
            default -> 0;
        };

        return (int) ((actorScore * topicScore * sourceImportance) / 100);
    }

}
