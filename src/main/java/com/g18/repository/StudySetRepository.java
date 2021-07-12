package com.g18.repository;

import com.g18.entity.Event;
import com.g18.entity.StudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
@Repository
public interface StudySetRepository extends JpaRepository<StudySet,Long> {
//    List<StudySet> findByTitleContains(String title, Pageable pageable);
    List<StudySet> findByCreatorId(Long id);

}
