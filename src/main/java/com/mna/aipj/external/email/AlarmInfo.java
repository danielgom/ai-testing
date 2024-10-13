package com.mna.aipj.external.email;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmInfo {

    private String actor;

    private String alertLevel;

    private String topic;

    private String title;

    private String content;
}
