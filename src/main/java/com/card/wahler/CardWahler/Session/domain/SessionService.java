package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    public Pokerman save(SessionService sessionService) {
        return sessionService.save(sessionService);
    }

    public Iterable<Session> findAll() {
        return sessionRepository.findAll();
    }

}
