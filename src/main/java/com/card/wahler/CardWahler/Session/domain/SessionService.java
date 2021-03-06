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

    public SessionDto joinSession(String password, String nick) {
        Session session = sessionRepository.findByPassword(password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no session for password"));
        Set<Pokerman> pokermen = session.getPokermen();
        Optional<Pokerman> byId = pokermanRepository.findByNick(nick, Pokerman.class);
        if (byId.isPresent()){
            pokermen.add(byId.get());
        } else {
            Pokerman newPokerman = builder().nick(nick).build();
            pokermanRepository.save(newPokerman);
            pokermen.add(newPokerman);
        }
        session.setPokermen(pokermen);
        return sessionMapper.sessionToSessionDto(sessionRepository.save(session));
    }

    public void leaveSession(Integer sessionId, String nick) {
        Optional<Session> byId = sessionRepository.findById(sessionId);
        if (byId.isPresent()) {
            Session session = byId.get();
            Set<Pokerman> pokermen = session.getPokermen();
            pokermen.removeIf(pokerman -> pokerman.getNick().equals(nick));
            session.setPokermen(pokermen);
            sessionRepository.save(session);
        }
    }

    public Set<SessionDto> findSessionsByNick(String nick) {
        Set<Session> no_user_for_ = pokermanRepository.findByNick(nick, Pokerman.class).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no user for ")
        ).getSesions();
        return  no_user_for_.stream().map(session -> sessionMapper.sessionToSessionDto(session)).collect(Collectors.toSet());
    }
}
