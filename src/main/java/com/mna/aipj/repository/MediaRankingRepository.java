package com.mna.aipj.repository;

import com.mna.aipj.model.MediaRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRankingRepository extends JpaRepository<MediaRanking, Integer> {

    Optional<MediaRanking> findByDomain(String domain);
}
