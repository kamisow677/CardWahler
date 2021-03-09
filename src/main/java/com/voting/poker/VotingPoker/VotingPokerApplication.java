package com.voting.poker.VotingPoker;

import com.voting.poker.VotingPoker.Pokerman.Pokerman;
import com.voting.poker.VotingPoker.Pokerman.PokermanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VotingPokerApplication {

	@Autowired
	PokermanRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(VotingPokerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		if (repository.findAll().iterator().hasNext() == false)
			repository.save(Pokerman.builder().nick("madora").build());
	}

}
