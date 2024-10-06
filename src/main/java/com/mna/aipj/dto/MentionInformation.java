package com.mna.aipj.dto;

import com.mna.aipj.model.AlertLevel;
import com.mna.aipj.model.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentionInformation {

    // Returned after analysis
    private String actor;

    // Returned after analysis
    private String actorProfession;

    private String title;

    private String snippet;

    private String domain;

    private String url;

    private String source;

    private LocalDateTime date;

    // Returned after analysis
    private Sentiment sentiment;

    // Returned after analysis
    private String topic;

    // Returned after analysis
    private AlertLevel alertLevel;

    // Returned after classification
    private boolean isImportant;

    private boolean isClassified;

    private boolean isAnalyzed;

}
