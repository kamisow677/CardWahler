package com.card.wahler.CardWahler.Pokerman;

import com.card.wahler.CardWahler.Answer.Answer;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class Pokerman {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String nick;

    private byte[] image;

    @ManyToMany(
            mappedBy = "pokermen",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.ALL
            })
    @JsonIgnore
    Set<Session> sesions;

    @OneToMany(mappedBy="pokerman")
    @JsonManagedReference
    private Set<Answer> answers;

    @JsonCreator
    public Pokerman(@JsonProperty("id") int id,
                    @JsonProperty("nick") String nick,
                    @JsonProperty("image") byte[] image,
                    @JsonProperty("sesions") Set<Session> sesions,
                    @JsonProperty("answers") Set<Answer> answers
    ) {
        this.id = id;
        this.nick = nick;
        this.image = image;
        this.sesions = sesions;
        this.answers = answers;
    }

}
