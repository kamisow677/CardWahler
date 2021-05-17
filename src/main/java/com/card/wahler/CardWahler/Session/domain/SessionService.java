package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Pokerman.PokermanRepository;
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.card.wahler.CardWahler.Pokerman.Pokerman.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PokermanRepository pokermanRepository;
    private final SessionMapper sessionMapper;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public Set<SessionDto> getAll() {
        Stream<Session> stream = StreamSupport.stream(sessionRepository.findAll().spliterator(), false);
        return stream.map(
                session -> sessionMapper.sessionToSessionDto(session)
        ).collect(Collectors.toSet());
    }

    public Iterable<Session> findAll() {
        return sessionRepository.findAll();
    }

    public void joinSession(String password, String keycloakUserId) {
        Session session = sessionRepository.findByPassword(password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no session for password"));
        Set<Pokerman> pokermen = session.getPokermen();
        Optional<Pokerman> byKeycloakUserId = pokermanRepository.findByKeycloakUserId(keycloakUserId, Pokerman.class);
        if (byKeycloakUserId.isPresent()){
            pokermen.add(byKeycloakUserId.get());
        } else {
            Pokerman newPokerman = builder().keycloakUserId(keycloakUserId).build();
            pokermanRepository.save(newPokerman);
            pokermen.add(newPokerman);
        }
        session.setPokermen(pokermen);
        sessionRepository.save(session);
    }

    public Object get(String keycloakUserId) {
        return null;
    }
}
