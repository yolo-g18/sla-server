package com.g18.repository;

import com.g18.entity.Card;
import com.g18.entity.CardLearning;
import com.g18.entity.User;
import com.g18.model.UserCardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CardLearningRepository extends JpaRepository<CardLearning,Long>{

    @Query("SELECT new com.g18.entity.CardLearning(cl.userCardId,cl.user,cl.card,cl.q,cl.intervalTime,cl.eFactor, cl.repetitionNumber,cl.hint,cl.learnedDate,cl.status,cl.color) FROM CardLearning cl WHERE cl.userCardId=:userCardId")
    CardLearning getCardLearningByUserCardId(@Param("userCardId") UserCardId userCardId);

    @Modifying
    @Query(value = "DELETE FROM sla_db.card_learning c WHERE c.card_id = :cardId", nativeQuery = true)
    void deleteAllCardLearning(@Param("cardId") Long cardId);

    CardLearning findCardLearningByCardAndUser(Card card, User user);

    List<CardLearning> findByUserAndLearnedDate(User user, Instant learnedDate);
}
