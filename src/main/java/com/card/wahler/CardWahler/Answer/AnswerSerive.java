package com.card.wahler.CardWahler.Answer;

import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.card.wahler.CardWahler.Pokerman.PokermanRepository;
import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AnswerSerive {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    PokermanRepository pokermanRepository;

    @Autowired
    AnswerMapper answerMapper;

    public Answer save(Answer answer){
        return answerRepository.save(answer);
    }

    public List<AnswerDto> findAll() {
        return StreamSupport.stream(answerRepository.findAll().spliterator(), false)
                .map(answer -> answerMapper.AnswerToAnswerDto(answer))
                .collect(Collectors.toList());
    }

    @Transactional
    public void editPoints(int points, String nick, String task) {
        int affectedRows = answerRepository.editPoints(points, nick, task);
        checkCorrectNumberOfRowsAffected(affectedRows);
    }

    @Transactional
    public void addAnswer(String nick, int points, String taskName) {
        Optional<Answer>  byTaskNameAndKeycloakId = answerRepository.findFirstByTaskNameAndNick(taskName, nick);
        if (byTaskNameAndKeycloakId.isPresent()) {
            Answer answer = byTaskNameAndKeycloakId.get();
            answer.setPoints(points);
            answerRepository.save(answer);
        } else {
            Optional<Pokerman> pokermanOptional = pokermanRepository.findByNick(nick, Pokerman.class);
            Pokerman pokerman = pokermanOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no pokerman"));

            Optional<Round> roundOptional = roundRepository.findByTaskName(taskName);
            Round round = roundOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "task name is bad"));

            Answer answer = Answer.builder().points(points)
                    .pokerman(pokerman)
                    .round(round)
                    .build();
            answerRepository.save(answer);
        }
    }

    private void checkCorrectNumberOfRowsAffected(Integer affectedRows) {
        if (affectedRows != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException("jakis sql", 1, affectedRows);
        }
    }

    public AnswerDto find(Integer roundId, String nick) {
        return answerRepository.findFirstByRoundIdAndNick(roundId, nick)
                .map(answer -> answerMapper.AnswerToAnswerDto(answer))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no answer "));
    }

    public AnswerDto findAnswerCurrentRoundInSession(Integer sessionId, String nick) {
        return answerRepository.findFirstByCurrentRoundInSession(sessionId, nick)
                .map(answer -> answerMapper.AnswerToAnswerDto(answer))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no answer "));
    }
}
