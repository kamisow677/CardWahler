package com.card.wahler.CardWahler.Pokerman;

import com.card.wahler.CardWahler.Session.domain.Session;
import com.card.wahler.CardWahler.Session.domain.SessionDto;
import org.springframework.stereotype.Component;

@Component
public class PokermanMapper {

    public PokermanDto pokermanToPokermanDto(Pokerman pokerman) {
        return PokermanDto.builder()
                .keycloakUserId(pokerman.getKeycloakUserId())
                .nick(pokerman.getNick())
                .image(pokerman.getImage())
//                .sesionsDto()
                .build();
    }

//    private SessionDto change(Session session) {
////        return new SessionDto.builder()
//    }

}
