package com.g18.repository;


import com.g18.dto.ProgressResponse;
import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning, Long>{
    List<StudySetLearning> findStudySetLearningByUserId(Long userId);
    StudySetLearning findStudySetLearningByStudySetAndUser(StudySet studySet, User user);
    List<StudySetLearning> findTop6StudySetLearningByUserId(Long userId);

    StudySetLearning findByUserIdAndStudySetId(Long userID,Long ssId);

    List<StudySetLearning> findByStudySetId(Long ssId);

    @Query(value = "SELECT AVG(rating) FROM study_set_learning  where study_set_id = ?1" , nativeQuery = true)
    double getAVGRatingSS(Long ssId);

    @Query("SELECT new com.g18.dto.ProgressResponse(a.username, s.studySet.id, s.progress, s.startDate) FROM StudySetLearning s INNER JOIN Account a ON s.user.id = a.user.id WHERE s.studySet.id =:studySetId")
    List<ProgressResponse> getListProgressByStudySet(@Param("studySetId") Long studySetId);

}
