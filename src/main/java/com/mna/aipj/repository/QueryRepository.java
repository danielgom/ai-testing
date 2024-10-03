package com.mna.aipj.repository;

import com.mna.aipj.model.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueryRepository extends JpaRepository<Query, Integer> {

    Optional<Query> findByQueryID(String queryID);
}
