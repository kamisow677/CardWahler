package com.card.wahler.CardWahler.Round;

import com.card.wahler.CardWahler.Answer.AnswerDto;
import com.card.wahler.CardWahler.Session.domain.SessionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Data;

import java.util.List;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class RoundDto {

    private int id;

    private String taskName;

    private Boolean isCurrent;

    SessionDto session;

    List<AnswerDto> answers;

}
