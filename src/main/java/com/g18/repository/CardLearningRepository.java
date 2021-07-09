package com.g18.repository;

import com.g18.dto.CardDto;
import com.g18.entity.CardLearning;
import com.g18.model.UserCardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardLearningRepository extends JpaRepository<CardLearning,Long>{

    @Query("SELECT new com.g18.dto.CardDto(cl.card.id,cl.card.studySet.id, cl.card.front,cl.card.back,cl.hint)" +
            " FROM CardLearning cl where cl.userCardId=:userCardId")
    CardDto getCardLearningByUserCardId(@Param("userCardId") UserCardId userCardId);
}
