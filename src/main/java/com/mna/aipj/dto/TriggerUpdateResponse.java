package com.mna.aipj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerUpdateResponse {

    @JsonProperty("got_from_api")
    private int gotFromAPI;

    @JsonProperty("total_classified")
    private int totalClassified;

    @JsonProperty("total_analysed")
    private int totalAnalysed;

    private boolean completed;

    private LocalDateTime completedAt;
}
