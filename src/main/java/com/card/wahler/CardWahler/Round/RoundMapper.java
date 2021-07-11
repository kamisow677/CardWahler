package com.card.wahler.CardWahler.Round;

import com.card.wahler.CardWahler.Answer.AnswerMapper;
import com.card.wahler.CardWahler.Pokerman.PokermanMapper;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.card.wahler.CardWahler.Session.domain.SessionDto;
import com.card.wahler.CardWahler.Session.domain.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoundMapper {

    private final AnswerMapper answerMapper;

    private final SessionMapper sessionMapper;

    public RoundDto roundToRoundDto(Round round) {
        return RoundDto.builder()
                .id(round.getId())
                .isCurrent(round.getIsCurrent())
                .taskName(round.getTaskName())
                .answers(
                        round.getAnswers().stream()
                                .map(answer -> answerMapper.AnswerToAnswerDto(answer))
                                .collect(Collectors.toList())
                )
                .session(sessionMapper.sessionToSessionDto(round.getSession()))
                .build();
    }

}
