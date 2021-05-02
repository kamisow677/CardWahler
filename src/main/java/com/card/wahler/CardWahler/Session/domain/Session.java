package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Session {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String password;

    @NotNull
    private String link;

    @ManyToMany
    Set<Pokerman> pokermens;

    @OneToMany(mappedBy="session")
    private Set<Round> rounds;

    @JsonCreator
    public Session(@JsonProperty("id") int id,
                         @JsonProperty("password") String password,
                         @JsonProperty("link") String link,
                        @JsonProperty("pokermens") String pokermens,
                   @JsonProperty("rounds") String rounds) {
        this.id = id;
        this.password = password;
        this.link = link;
        this.pokermens = null;
        this.rounds = null;
    }

}
