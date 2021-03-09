package com.voting.poker.VotingPoker.Session;

import com.voting.poker.VotingPoker.Round.Round;
import com.voting.poker.VotingPoker.Pokerman.Pokerman;
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

    @ManyToMany
    Set<Pokerman> pokermens;

    @OneToMany(mappedBy="session")
    private Set<Round> rounds;

}
