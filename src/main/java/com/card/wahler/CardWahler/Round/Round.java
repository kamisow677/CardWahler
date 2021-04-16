package com.card.wahler.CardWahler.Round;

import com.card.wahler.CardWahler.Answer.Answer;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Round {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        private String taskName;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "session_id")
        private Session session;

        @OneToMany(fetch = FetchType.EAGER, mappedBy="round")
        @JsonManagedReference
        private List<Answer> answers;



}
