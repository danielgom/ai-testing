package com.mna.aipj.repository;

import com.mna.aipj.model.MentionTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionTrendRepository extends JpaRepository<MentionTrend, Integer> {
}
