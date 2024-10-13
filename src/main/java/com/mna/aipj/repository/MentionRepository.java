package com.mna.aipj.repository;

import com.mna.aipj.model.Mention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Integer> {
    long countByCreatedAtAfter(LocalDateTime createdAt);
}
