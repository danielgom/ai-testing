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
@Table(name = "actor_mention_trends")
public class MentionTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private int frequency;

    @Column(name = "first_mention", nullable = false)
    private LocalDateTime firstMention;

    @Column(name = "last_mention", nullable = false)
    private LocalDateTime lastMention;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MentionTrend that = (MentionTrend) o;
        return frequency == that.frequency && Objects.equals(id, that.id) &&
                Objects.equals(actor, that.actor) && Objects.equals(topic, that.topic) &&
                Objects.equals(firstMention, that.firstMention) &&
                Objects.equals(lastMention, that.lastMention) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actor, topic, frequency, firstMention, lastMention, createdAt);
    }
}

