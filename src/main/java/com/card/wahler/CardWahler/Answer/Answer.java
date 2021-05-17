package com.card.wahler.CardWahler.Answer;

import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokerman_id")
    @JsonBackReference
    private Pokerman pokerman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
//    @JsonBackReference
    private Round round;

}
