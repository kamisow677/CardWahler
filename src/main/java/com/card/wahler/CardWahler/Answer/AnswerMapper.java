package com.card.wahler.CardWahler.Answer;


import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.card.wahler.CardWahler.Pokerman.PokermanDto;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerDto AnswerToAnswerDto(Answer answer) {
        return AnswerDto.builder()
                .points(answer.getPoints())
                .pokerman(
                        pokermanToPokermanDto(answer.getPokerman())
                )
                .build();
    }

    private PokermanDto pokermanToPokermanDto(Pokerman pokerman) {
        return PokermanDto.builder()
                .nick(pokerman.getNick())
                .build();
    }


}
