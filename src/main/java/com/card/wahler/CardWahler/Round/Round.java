package com.card.wahler.CardWahler.Round;

import com.card.wahler.CardWahler.Answer.Answer;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class Round {

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private int id;

        private String taskName;

        private Boolean isCurrent;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "session_id")
        private Session session;

        @OneToMany(fetch = FetchType.EAGER, mappedBy="round")
        private List<Answer> answers;

        @JsonCreator
        public Round(@JsonProperty("id") int id,
                        @JsonProperty("taskName") String taskName,
                        @JsonProperty("isCurrent") Boolean isCurrent,
                        @JsonProperty("session") Session session,
                        @JsonProperty("answers") List<Answer> answers
        ) {
                this.id = id;
                this.taskName = taskName;
                this.isCurrent = isCurrent;
                this.session = session;
                this.answers = answers;
        }

}
