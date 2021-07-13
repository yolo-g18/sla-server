package com.g18.repository;

import com.g18.entity.StudySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface StudySetRepository extends JpaRepository<StudySet,Long> {
    Page<StudySet> findByTitleContainsAndIsPublicTrue(String title, Pageable pageable);
    List<StudySet> findByCreatorId(Long id);

}
