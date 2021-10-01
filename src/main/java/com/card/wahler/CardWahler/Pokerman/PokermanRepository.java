package com.card.wahler.CardWahler.Pokerman;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokermanRepository extends CrudRepository<Pokerman, String> {

    <T> Optional<T> findByNick(String nick, Class<T> type);

    <T> Optional<T> findById(String id, Class<T> type);

    @Modifying
    @Query("update Pokerman p set p.image = ?1 where p.nick = ?2")
    int updatePokermanImage(byte[] image, String nick);

    @Modifying
    @Query("update Pokerman p set p.image = ?1 where p.nick = ?2")
    void leave(String Id, Long sessionId);
}

interface ImageOnly {
    byte[] getImage();
}