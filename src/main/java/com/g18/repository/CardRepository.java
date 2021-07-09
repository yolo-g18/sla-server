package com.g18.repository;

import com.g18.dto.CardDto;
import com.g18.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Long>{

    @Query("SELECT new com.g18.dto.CardDto(c.id, c.studySet.id, c.front, c.back, cl.hint) " +
            "FROM Card c INNER JOIN CardLearning cl ON c.id = cl.card.id where c.studySet.id=:studySetId")
    List<CardDto> getListCardByStudySet(@Param("studySetId")Long studySetId);
}
