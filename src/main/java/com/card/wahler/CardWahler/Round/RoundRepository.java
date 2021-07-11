package com.card.wahler.CardWahler.Round;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoundRepository extends CrudRepository<Round, String> {

    Optional<Round> findByTaskName(String taskName);

    Set<Round> findBySessionId(int sessionId);
}
