package com.g18.repository;


import com.g18.dto.ProgressResponse;
import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning, Long>{
    @Query("SELECT ssl FROM StudySetLearning ssl WHERE ssl.studySet.isActive = TRUE AND ssl.user.id = :userId")
    List<StudySetLearning> findStudySetLearningByUserId(@Param("userId") Long userId);
//    List<StudySetLearning> findStudySetLearningByUserId(Long userId);

    @Query("SELECT ssl FROM StudySetLearning ssl WHERE ssl.studySet.isActive = TRUE AND ssl.studySet = :studySet And ssl.user = :user")
    StudySetLearning findStudySetLearningByStudySetAndUser(@Param("studySet") StudySet studySet,@Param("user") User user);
//    StudySetLearning findStudySetLearningByStudySetAndUser(StudySet studySet, User user);

    @Query("SELECT ssl FROM StudySetLearning ssl WHERE ssl.studySet.isActive = TRUE AND ssl.user.id = :userId")
    List<StudySetLearning> findTop6StudySetLearningByUserId(@Param("userId") Long userId, Pageable pageable);
//    List<StudySetLearning> findTop6StudySetLearningByUserId(Long userId);

    @Query("SELECT ssl FROM StudySetLearning ssl WHERE ssl.studySet.isActive = TRUE AND ssl.studySet.id = :ssId And ssl.user.id = :userID")
    StudySetLearning findByUserIdAndStudySetId(@Param("userID") Long userID, @Param("ssId") Long ssId);
//    StudySetLearning findByUserIdAndStudySetId(Long userID,Long ssId);

    @Query("SELECT ssl FROM StudySetLearning ssl WHERE ssl.studySet.isActive = TRUE AND ssl.studySet.id = :ssId")
    List<StudySetLearning> findByStudySetId(@Param("ssId") Long ssId);
//    List<StudySetLearning> findByStudySetId(Long ssId);

    @Query(value = "SELECT AVG(rating) FROM study_set_learning  where study_set_id = ?1" , nativeQuery = true)
    double getAVGRatingSS(Long ssId);

    @Query("SELECT new com.g18.dto.ProgressResponse(a.username, s.studySet.id, s.progress, s.startDate) FROM StudySetLearning s " +
            "INNER JOIN Account a ON s.user.id = a.user.id WHERE s.studySet.id =:studySetId AND s.studySet.isActive = TRUE")
    List<ProgressResponse> getListProgressByStudySet(@Param("studySetId") Long studySetId);

}
