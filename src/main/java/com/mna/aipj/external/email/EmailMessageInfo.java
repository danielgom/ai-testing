package com.mna.aipj.external.email;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageInfo {

    private String toEmail;

    private List<AlarmInfo> alarmInfoList;

    private long totalMentions;

    private long totalAnalysed;

    private long mentionsSinceLastDate;

    private long analysedSinceLastDate;

    private LocalDateTime sinceLastDate;

}
