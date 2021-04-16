package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private int password;

    @NotNull
    private String link;

    @ManyToMany
    Set<Pokerman> pokermens;

    @OneToMany(mappedBy="session")
    private Set<Round> rounds;

}
