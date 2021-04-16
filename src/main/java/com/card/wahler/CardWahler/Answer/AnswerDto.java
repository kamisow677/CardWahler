package com.card.wahler.CardWahler.Answer;

import com.card.wahler.CardWahler.Pokerman.PokermanDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {

    private int points;

    private PokermanDto pokerman;

}
