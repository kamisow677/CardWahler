package com.voting.poker.VotingPoker.Pokerman;

import com.voting.poker.VotingPoker.Answer.Answer;
import com.voting.poker.VotingPoker.Session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pokerman {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String nick;

    private int image_id;

    @ManyToMany
    Set<Session> sesions;

    @OneToMany(mappedBy="pokerman")
    private Set<Answer> answers;

}
