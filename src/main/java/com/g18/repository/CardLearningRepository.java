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

    @Query("SELECT new com.g18.entity.CardLearning(cl.userCardId,cl.user,cl.card,cl.q,cl.intervalTime,cl.eFactor, cl.repetitionNumber,cl.hint,cl.learnedDate,cl.status,cl.color) " +
            "FROM CardLearning cl WHERE cl.userCardId=:userCardId AND cl.card.studySet.isActive = TRUE")
    CardLearning getCardLearningByUserCardId(@Param("userCardId") UserCardId userCardId);

    @Query("SELECT cl FROM CardLearning cl WHERE cl.card.studySet.isActive = TRUE AND cl.card = :card and cl.user = :user")
    CardLearning findCardLearningByCardAndUser(@Param("card") Card card, @Param("user") User user);

    @Query(value = "SELECT cl.* FROM card_learning cl " +
            "INNER JOIN card c ON cl.card_id = c.id " +
            "INNER JOIN study_set ss ON c.study_set_id = ss.id " +
            "WHERE c.study_set_id = :studySetId AND ss.is_active = True " +
            "AND cl.user_id = :userId AND cl.learned_date LIKE :learnedDate%", nativeQuery = true)
    List<CardLearning> getListCardLearningByStudySetIdAndUserIdAndDate(Long studySetId, Long userId, String learnedDate);

    @Query("SELECT new com.g18.dto.CardLearningDto(cl.user.id, c.id, c.studySet.id, c.front, c.back, cl.hint, cl.color, cl.q) FROM Card c " +
            "INNER JOIN CardLearning cl ON c.id = cl.card.id " +
            "WHERE cl.card.studySet.isActive = TRUE AND cl.user.id = :userId AND c.studySet.id = :studySetId ORDER BY cl.q ASC")
    List<CardLearningDto> getListCardLearningOrderByQ(@Param("userId")Long userId, @Param("studySetId")Long studySetId);

    @Query("SELECT new com.g18.dto.CardLearningDto(cl.user.id, c.id, c.studySet.id, c.front, c.back, cl.hint, cl.color, cl.q) FROM Card c " +
            "INNER JOIN CardLearning cl ON c.id = cl.card.id " +
            "WHERE cl.card.studySet.isActive = TRUE AND cl.user.id = :userId AND c.studySet.id = :studySetId ORDER BY cl.learnedDate ASC")

    List<CardLearningDto> getTopCardLearning(@Param("userId")Long userId, @Param("studySetId")Long studySetId, Pageable pageable);

    @Query("SELECT cl FROM CardLearning cl WHERE cl.card.studySet.isActive = TRUE AND cl.learnedDate < :learnedDate")
    List<CardLearning> findCardLearningByLearnedDateBefore(@Param("learnedDate") Instant learnedDate);
}
