package com.g18.repository;


import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning, Long>{
    List<StudySetLearning> findStudySetLearningByUserId(Long userId);
    StudySetLearning findStudySetLearningByStudySetAndUser(StudySet studySet, User user);
    List<StudySetLearning> findTop6StudySetLearningByUserId(Long userId);

    StudySetLearning findByUserIdAndStudySetId(Long userID,Long ssId);

    List<StudySetLearning> findByStudySetId(Long ssId);

    @Query(value = "SELECT AVG(rating) FROM sla_db.study_set_learning  where study_set_id = ?1" , nativeQuery = true)
    double getAVGRatingSS(Long ssId);
}
