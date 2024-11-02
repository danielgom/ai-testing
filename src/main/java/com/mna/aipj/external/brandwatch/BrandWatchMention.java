package com.mna.aipj.external.brandwatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BrandWatchMention {

    @JsonProperty("title")
    private String title;

    @JsonProperty("snippet")
    private String snippet;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("originalUrl")
    private String url;

    @JsonProperty("contentSourceName")
    private String contentSourceName;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime mentionDate;

    @JsonProperty("added")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime addedMentionDate;
}
