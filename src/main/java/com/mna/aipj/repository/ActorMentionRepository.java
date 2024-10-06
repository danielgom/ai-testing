package com.mna.aipj.repository;

import com.mna.aipj.model.ActorMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorMentionRepository extends JpaRepository<ActorMention, Integer> {
}
