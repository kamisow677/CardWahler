package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Pokerman.PokermanDto;
import lombok.*;

import java.util.Set;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class SessionDto {

    private int id;

    private String password;

    private String link;

    Set<PokermanDto> pokermen;

}
