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
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_level", nullable = false)
    @ColumnTransformer(write = "?::alert_level_enum")
    private AlertLevel alertLevel;

    @Column(nullable = true)
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
        return id == alert.id && active == alert.active &&
                Objects.equals(actor, alert.actor) &&
                Objects.equals(topic, alert.topic) && alertLevel == alert.alertLevel &&
                Objects.equals(triggerDate, alert.triggerDate) &&
                Objects.equals(deactivatedAt, alert.deactivatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actor, topic, alertLevel,
                active, triggerDate, deactivatedAt);
    }
}
