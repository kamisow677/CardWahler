package com.card.wahler.CardWahler.Session.domain;

import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.card.wahler.CardWahler.Round.Round;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Validated
public class Session {

    @Id
    @GeneratedValue
    private int id;

    @NotNull()
    private String password;

    private String link;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.ALL
            }
    )
    @JsonIgnore
    @JoinTable(
            name = "pokermen_sessions",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "keycloakUserId_id"))
    private Set<Pokerman> pokermen;

    @OneToMany(mappedBy="session")
    private Set<Round> rounds;

    @JsonCreator
    public Session(@JsonProperty("id") int id,
                   @NotNull @JsonProperty("password") String password,
                   @JsonProperty("link") String link,
                   @JsonProperty("pokermen") Set<Pokerman> pokermen,
                   @JsonProperty("rounds") Set<Round> rounds) {
        this.id = id;
        this.password = password;
        this.link = link;
        this.pokermen = pokermen;
        this.rounds = rounds;
    }

}
