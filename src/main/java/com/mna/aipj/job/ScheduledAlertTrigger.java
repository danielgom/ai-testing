package com.mna.aipj.job;

import com.mna.aipj.external.email.AlarmInfo;
import com.mna.aipj.external.email.EmailMessageInfo;
import com.mna.aipj.external.email.EmailService;
import com.mna.aipj.model.ActorMention;
import com.mna.aipj.model.Alert;
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

    private static final int DEFAULT_UNIMPORTANT_ALERT_WEIGHT = 0;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledAlertTrigger.class);

    @Scheduled(cron = "0 0/5 * * * ?", zone = "America/Mexico_City") // Every 5 minutes execution
    @Transactional
    public void generateAlerts() {
        logger.info("initializing alert trigger...");
        List<Alert> unTriggeredAlerts =
                alertRepository.findByActiveFalseAndTriggerDateNullAndDeactivatedAtNullAndWeighingNot(DEFAULT_UNIMPORTANT_ALERT_WEIGHT);

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
                            .alertLevel(getAlertLevel(alert))
                            .url(actorMention.getUrlSource())
                            .topic(actorMention.getTopic())
                            .title(actorMention.getTitle())
                            .content(actorMention.getContent())
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

    private String getAlertLevel(Alert alert) {
        int weighing = alert.getWeighing();
        // These can be dynamically set later
        if (weighing <= 24) {
            return "LOW";
        }
        if (weighing <= 74) {
            return "MEDIUM";
        }
        return "HIGH";
    }
}
