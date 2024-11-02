package com.mna.aipj.job;

import com.mna.aipj.model.Query;
import com.mna.aipj.repository.QueryRepository;
import com.mna.aipj.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledMentionPull {

    private final AIService aiService;

    private final QueryRepository queryRepository;

    @Scheduled(cron = "0 2/2 * * * ?", zone = "America/Mexico_City")
    public void triggerMentionPull() {
        List<Query> queries = queryRepository.findAll();

        queries.forEach(query -> aiService.pullClassifyAndAnaliseMentions(query.getQueryID()));
    }
}
