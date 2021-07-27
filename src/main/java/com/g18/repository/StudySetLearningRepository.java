package com.g18.repository;


import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning, Long>{
    List<StudySetLearning> findStudySetLearningByUserId(Long userId);
    StudySetLearning findStudySetLearningByStudySetAndUser(StudySet studySet, User user);
    List<StudySetLearning> findTop6StudySetLearningByUserId(Long userId);
}
