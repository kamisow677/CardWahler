package com.card.wahler.CardWahler.Pokerman;

import org.springframework.stereotype.Component;

@Component
public class PokermanMapper {

    public PokermanDto pokermanToPokermanDto(Pokerman pokerman) {
        return PokermanDto.builder()
                .id(pokerman.getId())
                .nick(pokerman.getNick())
                .image(pokerman.getImage())
                .build();
    }

}
