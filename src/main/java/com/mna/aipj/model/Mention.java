package com.mna.aipj.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mentions")
public class Mention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "platform_source", nullable = false)
    private String platformSource;

    @Column(name = "url_source", nullable = false)
    private String urlSource;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "mention_date", nullable = false)
    private LocalDateTime mentionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mention mention = (Mention) o;
        return id == mention.id && Objects.equals(platformSource, mention.platformSource) &&
                Objects.equals(urlSource, mention.urlSource) && Objects.equals(type, mention.type) &&
                Objects.equals(content, mention.content) &&
                Objects.equals(mentionDate, mention.mentionDate) && Objects.equals(createdAt, mention.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, platformSource, urlSource, type, content, mentionDate, createdAt);
    }
}
