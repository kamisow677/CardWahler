package com.card.wahler.CardWahler.Answer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, String> {

    @Modifying
    @Query(value = "update answer set points = ?1 " +
            "WHERE round_id =  " +
            "(SELECT r.id FROM round r " +
            "WHERE r.task_name = ?3)" +
            "AND pokerman_id = " +
            "(SELECT p.keycloak_user_id FROM pokerman p " +
            "WHERE p.keycloak_user_id = ?2)", nativeQuery = true)
    int editPoints(int points, String keycloakId, String taskName);

    @Query(value = "SELECT * FROM answer " +
            "WHERE round_id = " +
            "(SELECT r.id FROM round r " +
            "WHERE r.task_name = ?1)" +
            "AND pokerman_id = ?2", nativeQuery = true)
    Optional<Answer> findFirstByTaskNameAndKeycloakId(String taskName, String keycloakId);

    @Query(value = "SELECT * FROM answer " +
            "WHERE round_id = ?1 " +
            "AND pokerman_id = ?2", nativeQuery = true)
    Optional<Answer> findFirstByRoundIdAndKeycloakId(Integer roundId, String keycloakId);

    @Query(value = "SELECT * FROM answer " +
            "WHERE round_id = (" +
            "   SELECT r.id FROM round r" +
            "   WHERE r.session_id = ?1  AND is_current = true" +
            ")" +
            "AND pokerman_id = ?2", nativeQuery = true)
    Optional<Answer> findFirstByCurrentRoundInSession(Integer sessionId, String keycloakId);


}
