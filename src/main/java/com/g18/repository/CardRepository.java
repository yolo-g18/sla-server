package com.g18.repository;

import com.g18.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findTop4ByStudySetId(Long studySetId);

    @Query(value ="SELECT count(id) FROM sla_db.card where study_set_id = ?1",nativeQuery = true)
    int countNumberCardBySSID(long studySetId);

}

