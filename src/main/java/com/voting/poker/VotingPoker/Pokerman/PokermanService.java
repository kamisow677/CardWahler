package com.voting.poker.VotingPoker.Pokerman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokermanService {

    @Autowired
    PokermanRepository repository;

    public Pokerman save(Pokerman pokerman) {
        return repository.save(pokerman);
    }

    public Iterable<Pokerman> findAll() {
        return repository.findAll();
    }

}
