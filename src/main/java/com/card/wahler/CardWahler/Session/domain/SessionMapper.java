package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Pokerman.PokermanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionMapper {

    private final PokermanMapper pokermanMapper;

    public SessionDto sessionToSessionDto(Session session) {
        return SessionDto.builder()
                .id(session.getId())
                .link(session.getLink())
                .password(session.getPassword())
                .pokermen(
                        session.getPokermen().stream()
                                .map(pokerman -> pokermanMapper.pokermanToPokermanDto(pokerman))
                                .collect(Collectors.toSet())
                )
                .build();
    }

}
