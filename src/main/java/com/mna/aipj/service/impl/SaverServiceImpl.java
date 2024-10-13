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

    private static final Logger logger = LoggerFactory.getLogger(SaverServiceImpl.class);
    private final ActorMentionRepository actorMentionRepository;

    @Override
    public void saveUpdateQuery(String queryID, LocalDateTime lastMentionDate) {
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

            alertRepository.save(Alert.builder()
                    .actorMention(actorMention)
                    .topic(mentionInformation.getTopic())
                    .alertLevel(mentionInformation.getAlertLevel())
                    .build());
        });
    }
}
