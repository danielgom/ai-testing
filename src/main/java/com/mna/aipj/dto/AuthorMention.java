package com.mna.aipj.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthorMention {

    private LocalDateTime date;

    private String title;

    private String snippet;

    private String fullText;

    private String url;

    private String sentiment;

}
