package com.g18.repository;

import com.g18.entity.StudySetLearning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySetLearningRepository extends JpaRepository<StudySetLearning,Long> {
    List<StudySetLearning> findStudySetLearningByUserId(Long userId);

}
