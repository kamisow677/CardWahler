package com.voting.poker.VotingPoker.Answer;

import com.voting.poker.VotingPoker.Round.Round;
import com.voting.poker.VotingPoker.Pokerman.Pokerman;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Answer {

    @Id
    @GeneratedValue
    private int id;

    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokerman_id")
    private Pokerman pokerman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
    private Round round;

}
