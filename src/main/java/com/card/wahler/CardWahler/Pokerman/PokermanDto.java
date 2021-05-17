package com.card.wahler.CardWahler.Pokerman;

import com.card.wahler.CardWahler.Session.domain.SessionDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PokermanDto {

    private String keycloakUserId;

    private String nick;

    private byte[] image;

    Set<SessionDto> sesionsDto;

}
