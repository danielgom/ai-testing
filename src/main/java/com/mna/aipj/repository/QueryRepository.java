package com.mna.aipj.repository;

import com.mna.aipj.model.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueryRepository extends JpaRepository<Query, Integer> {

    Optional<Query> findByQueryID(String queryID);

    List<Query> findAllByLastMentionDateNotNullOrderByLastMentionDateDesc();

    @Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE Query q SET q.lastMentionDate = :lastMentionDate WHERE q.queryID = :queryID")
    void updateLastMentionDate(@Param("queryID") String queryID, @Param("lastMentionDate") LocalDateTime lastMentionDate);
}
