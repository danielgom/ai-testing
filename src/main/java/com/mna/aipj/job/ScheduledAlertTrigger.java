package com.mna.aipj.job;

import com.mna.aipj.external.email.AlarmInfo;
import com.mna.aipj.external.email.EmailMessageInfo;
import com.mna.aipj.external.email.EmailService;
import com.mna.aipj.model.ActorMention;
import com.mna.aipj.model.Alert;
import com.mna.aipj.model.AlertLevel;
import com.mna.aipj.model.Query;
import com.mna.aipj.repository.ActorMentionRepository;
import com.mna.aipj.repository.AlertRepository;
import com.mna.aipj.repository.MentionRepository;
import com.mna.aipj.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledAlertTrigger {

    private final EmailService emailService;

    private final AlertRepository alertRepository;

    private final QueryRepository queryRepository;

    private final MentionRepository mentionRepository;

    private final ActorMentionRepository actorMentionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledAlertTrigger.class);

    @Scheduled(cron = "0 */1 * * * *") // Every 10 minutes execution
    @Transactional
    public void generateAlerts() {
        logger.info("initializing alert trigger...");
        List<Alert> unTriggeredAlerts =
                alertRepository.findByActiveFalseAndTriggerDateNullAndDeactivatedAtNullAndAlertLevelNot(AlertLevel.NONE.name());

        if (unTriggeredAlerts.isEmpty()) {
            logger.info("No alerts to trigger");
            return;
        }

        List<Query> queryList =
                queryRepository.findAllByLastMentionDateNotNullOrderByLastMentionDateDesc();
        LocalDateTime lastUpdateDate = queryList.getFirst().getLastMentionDate();

        long totalMentions = mentionRepository.count();
        long totalAnalysed = actorMentionRepository.count();

        long mentionsSinceLastDate = mentionRepository.countByCreatedAtAfter(lastUpdateDate);

        LocalDateTime now = LocalDateTime.now();
        List<AlarmInfo> alarmList = unTriggeredAlerts.stream()
                .peek(alert -> alertRepository.updateTriggerDateAndSetActiveTrue(now, alert.getId()))
                .map(alert -> {
                    ActorMention actorMention = alert.getActorMention();
                    return AlarmInfo.builder()
                            .actor(actorMention.getActor().getName())
                            .alertLevel(alert.getAlertLevel().name())
                            .topic(alert.getTopic())
                            .title(alert.getActorMention().getTitle())
                            .content(alert.getActorMention().getContent())
                            .build();
                })
                .toList();

        EmailMessageInfo emailMessageInfo = EmailMessageInfo.builder()
                .toEmail("A0179498@tec.mx")
                .sinceLastDate(lastUpdateDate)
                .totalMentions(totalMentions)
                .totalAnalysed(totalAnalysed)
                .mentionsSinceLastDate(mentionsSinceLastDate)
                .analysedSinceLastDate(unTriggeredAlerts.size())
                .alarmInfoList(alarmList)
                .build();

        emailService.sendSimpleEmail(emailMessageInfo);
        logger.info("successfully sent last triggered alerts at {}", lastUpdateDate);
    }
}
