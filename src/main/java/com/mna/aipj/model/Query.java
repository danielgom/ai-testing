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
@Table(name = "queries")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "query_id")
    private String queryID;

    @Column(name = "last_mention_date")
    private LocalDateTime lastMentionDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return id == query.id && Objects.equals(queryID, query.queryID) &&
                Objects.equals(lastMentionDate, query.lastMentionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, queryID, lastMentionDate);
    }
}
