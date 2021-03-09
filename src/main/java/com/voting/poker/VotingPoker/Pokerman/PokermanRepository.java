package com.voting.poker.VotingPoker.Pokerman;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokermanRepository extends CrudRepository<Pokerman, String> {
}
