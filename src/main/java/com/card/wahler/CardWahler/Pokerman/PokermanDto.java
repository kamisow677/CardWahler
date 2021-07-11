package com.card.wahler.CardWahler.Pokerman;

import com.card.wahler.CardWahler.Session.domain.SessionDto;
import lombok.*;

import java.util.Set;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class PokermanDto {

    private String keycloakUserId;

    private String nick;

    private byte[] image;

    Set<SessionDto> sesionsDto;

}
