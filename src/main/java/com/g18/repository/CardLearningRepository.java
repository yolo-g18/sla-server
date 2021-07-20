package com.g18.repository;

import com.g18.dto.CardLearningDto;
import com.g18.entity.Card;
import com.g18.entity.CardLearning;
import com.g18.entity.User;
import com.g18.model.UserCardId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CardLearningRepository extends JpaRepository<CardLearning,Long>{

    @Query("SELECT new com.g18.entity.CardLearning(cl.userCardId,cl.user,cl.card,cl.q,cl.intervalTime,cl.eFactor, cl.repetitionNumber,cl.hint,cl.learnedDate,cl.status,cl.color) FROM CardLearning cl WHERE cl.userCardId=:userCardId")
    CardLearning getCardLearningByUserCardId(@Param("userCardId") UserCardId userCardId);

    CardLearning findCardLearningByCardAndUser(Card card, User user);

    List<CardLearning> findByUserAndLearnedDate(User user, Instant learnedDate);

    @Query("SELECT new com.g18.dto.CardLearningDto(cl.user.id, c.id, c.studySet.id, c.front, c.back, cl.hint, cl.color) FROM Card c\n" +
        "INNER JOIN CardLearning cl ON c.id = cl.card.id  WHERE cl.user.id = :userId AND c.studySet.id = :studySetId ORDER BY cl.learnedDate ASC")
    List<CardLearningDto> getTop20CardLearning(@Param("userId")Long userId, @Param("studySetId")Long studySetId, Pageable pageable);
}
