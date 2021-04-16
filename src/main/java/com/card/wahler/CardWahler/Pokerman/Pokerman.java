package com.card.wahler.CardWahler.Pokerman;

import com.card.wahler.CardWahler.Answer.Answer;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
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

    private byte[] image;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Session> sesions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="pokerman")
    @JsonManagedReference
    private List<Answer> answers;

}
