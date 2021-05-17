package com.card.wahler.CardWahler.Answer;

import com.card.wahler.CardWahler.Pokerman.PokermanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerMapper {

    private final PokermanMapper pokermanMapper;

    public AnswerDto AnswerToAnswerDto(Answer answer) {
        return AnswerDto.builder()
                .points(answer.getPoints())
                .pokerman(
                        pokermanMapper.pokermanToPokermanDto(answer.getPokerman())
                )
                .build();
    }

}
