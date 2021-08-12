package com.g18.repository;

import com.g18.entity.Card;
import com.g18.entity.StudySet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    @Query("SELECT c FROM Card c WHERE c.studySet.isActive = TRUE AND c.studySet.id = :studySetId")
    List<Card> findTop4ByStudySetId(@Param("studySetId") Long studySetId, Pageable pageable);
//    List<Card> findTop4ByStudySetId(Long studySetId);

//    @Query(value ="SELECT count(id) FROM card where study_set_id = ?1",nativeQuery = true)
    @Query("SELECT COUNT(c.id) FROM Card c where c.studySet.id = ?1 AND c.studySet.isActive = true")
    int countNumberCardBySSID(long studySetId);

    @Query("SELECT c FROM Card c WHERE c.id = :cardId AND c.studySet.isActive = TRUE")
    Card findByCardId(@Param("cardId") Long cardId);
}

