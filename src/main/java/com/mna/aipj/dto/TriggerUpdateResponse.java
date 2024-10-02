package com.mna.aipj.dto;

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

    private String message;

    private boolean completed;

    private LocalDateTime completedAt;
}
