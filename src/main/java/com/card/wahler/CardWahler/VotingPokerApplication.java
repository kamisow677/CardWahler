package com.card.wahler.CardWahler;

import com.card.wahler.CardWahler.Answer.AnswerRepository;
import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.card.wahler.CardWahler.Round.Round;
import com.card.wahler.CardWahler.Pokerman.PokermanRepository;
import com.card.wahler.CardWahler.Round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class VotingPokerApplication {

	@Autowired
	PokermanRepository repository;

	@Autowired
    AnswerRepository answerRepository;

	@Autowired
	RoundRepository roundRepository;

	public static void main(String[] args) {
		SpringApplication.run(VotingPokerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		if (repository.findAll().iterator().hasNext() == false) {
			repository.save(Pokerman.builder().nick("madora").build());

//			answerRepository.save(Answer.builder().id(30).build());
			roundRepository.save(Round.builder().id(1).taskName("jira-1000").build());
		}
	}

}
