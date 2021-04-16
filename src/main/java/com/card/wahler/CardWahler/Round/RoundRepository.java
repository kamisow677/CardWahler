package com.card.wahler.CardWahler.Round;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoundRepository extends CrudRepository<Round, String> {
    Optional<Round> findById(int i);

    Optional<Round> findByTaskName(String taskName);
}
