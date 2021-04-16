package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
}
