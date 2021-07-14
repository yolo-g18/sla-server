package com.g18.repository;

import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning, Long>{

    StudySetLearning findStudySetLearningByStudySetAndUser(StudySet studySet, User user);

    @Modifying
    @Query(value = "DELETE FROM sla_db.study_set_learning s WHERE s.study_set_id = :studySetId", nativeQuery = true)
    void deleteAllStudySetLearning(@Param("studySetId") Long studySetId);

    List<StudySetLearning> findStudySetLearningsByUser(User user);

}
