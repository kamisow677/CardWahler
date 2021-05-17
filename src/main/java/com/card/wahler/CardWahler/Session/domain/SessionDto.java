package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Pokerman.PokermanDto;
import lombok.Data;
import lombok.Builder;

import java.util.Set;

@Data
@Builder
public class SessionDto {

    private int id;

    private String password;

    private String link;

    Set<PokermanDto> pokermen;

}
