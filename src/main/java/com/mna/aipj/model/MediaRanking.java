package com.mna.aipj.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_ranking")
public class MediaRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String media;

    private String domain;

    private String country;

    private String region;

    private String language;

    private String typology;

    @Column(name = "global_rank")
    private int globalRank;

    private double overall;

    private double score;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaRanking that = (MediaRanking) o;
        return id == that.id && globalRank == that.globalRank && Double.compare(overall, that.overall) == 0 && Double.compare(score, that.score) == 0 && Objects.equals(media, that.media) && Objects.equals(domain, that.domain) && Objects.equals(country, that.country) && Objects.equals(region, that.region) && Objects.equals(language, that.language) && Objects.equals(typology, that.typology);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, media, domain, country, region, language, typology, globalRank, overall, score);
    }
}
