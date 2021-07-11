package com.card.wahler.CardWahler.Round;

import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository repository;
    private final SessionRepository sessionRepository;
    private final RoundMapper roundMapper;

    public RoundDto getCurrentRound(Integer sessionId) {
        return repository.findBySessionId(sessionId)
                .stream().filter(it -> it.getIsCurrent() == true)
                .findFirst()
                .map(round -> roundMapper.roundToRoundDto(round))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no user for "));
    }
}
