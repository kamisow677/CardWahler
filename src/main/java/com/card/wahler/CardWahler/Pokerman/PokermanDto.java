package com.card.wahler.CardWahler.Pokerman;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class PokermanDto {

    @NotNull
    private String nick;

}
