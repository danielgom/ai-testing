package com.mna.aipj.external.brandwatch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandwatchMentionResponse {

    @JsonProperty("results")
    private List<BrandWatchMention> results;
}

