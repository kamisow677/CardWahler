package com.voting.poker.VotingPoker.Round;

import com.voting.poker.VotingPoker.Session.Session;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Round {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String taskName;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "session_id")
        private Session session;


}
