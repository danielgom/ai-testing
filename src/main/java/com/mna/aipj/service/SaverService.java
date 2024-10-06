package com.mna.aipj.service;

import com.mna.aipj.dto.MentionInformation;

import java.time.LocalDateTime;
import java.util.List;

public interface SaverService {

    void saveUpdateQuery(String queryID, LocalDateTime lastMentionDate);

    void saveUnimportantMentions(List<MentionInformation> mentions);

    void saveAnalisedMentions(List<MentionInformation> mentions);
}
