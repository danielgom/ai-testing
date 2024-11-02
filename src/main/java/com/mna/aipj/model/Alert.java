package com.mna.aipj.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "actor_mention_id", nullable = false)
    private ActorMention actorMention;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_importance", nullable = false, columnDefinition = "importance_level_enum")
    @ColumnTransformer(write = "?::importance_level_enum")
    private ActorImportance actorImportance;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_importance", nullable = false, columnDefinition = "importance_level_enum")
    @ColumnTransformer(write = "?::importance_level_enum")
    private TopicImportance topicImportance;

    @Column(name = "source_importance", nullable = false)
    private Double sourceImportance;

    @Column(name = "weighing", nullable = false)
    private int weighing;

    private boolean active;

    @Column(name = "trigger_date", nullable = false)
    private LocalDateTime triggerDate;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return id == alert.id && weighing == alert.weighing && active == alert.active &&
                Objects.equals(actorMention, alert.actorMention) &&
                actorImportance == alert.actorImportance &&
                topicImportance == alert.topicImportance &&
                Objects.equals(sourceImportance, alert.sourceImportance) &&
                Objects.equals(triggerDate, alert.triggerDate) && Objects.equals(deactivatedAt, alert.deactivatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actorMention, actorImportance, topicImportance, sourceImportance,
                weighing, active, triggerDate, deactivatedAt);
    }
}
