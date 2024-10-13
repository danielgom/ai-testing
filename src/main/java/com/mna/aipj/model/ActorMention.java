package com.mna.aipj.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actor_mentions")
public class ActorMention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Column(name = "platform_source", nullable = false)
    private String platformSource;

    @Column(name = "url_source", nullable = false)
    private String urlSource;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment", nullable = false)
    @ColumnTransformer(write = "?::emotion_enum")
    private Sentiment sentiment;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String topic;

    @Column(name = "mention_date", nullable = false)
    private LocalDateTime mentionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorMention mention = (ActorMention) o;
        return id == mention.id && Objects.equals(actor, mention.actor) &&
                Objects.equals(platformSource, mention.platformSource) &&
                Objects.equals(urlSource, mention.urlSource) &&
                Objects.equals(type, mention.type) && Objects.equals(content, mention.content) &&
                Objects.equals(topic, mention.topic) && Objects.equals(mentionDate, mention.mentionDate) &&
                Objects.equals(createdAt, mention.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actor, platformSource, urlSource, type,
                content, topic, mentionDate, createdAt);
    }
}
