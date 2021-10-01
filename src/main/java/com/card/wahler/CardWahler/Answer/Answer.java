package com.card.wahler.CardWahler.Answer;

import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokerman_id")
    @JsonBackReference
    private Pokerman pokerman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
    private Round round;

    @JsonCreator
    @Email
    public Answer(@JsonProperty("id") int id,
                 @JsonProperty("points") int points,
                 @JsonProperty("pokerman") Pokerman pokerman,
                 @JsonProperty("round") Round round
    ) {
        this.id = id;
        this.points = points;
        this.pokerman = pokerman;
        this.round = round;
    }

}
