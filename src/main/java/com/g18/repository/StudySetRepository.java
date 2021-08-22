package com.g18.repository;

import com.g18.entity.StudySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudySetRepository extends JpaRepository<StudySet,Long> {
//    Page<StudySet> findByTitleContainsAndIsPublicTrue(String title, Pageable pageable);
    Page<StudySet> findByTitleContainsAndIsPublicTrueAndIsActiveTrue(String title, Pageable pageable);

//    List<StudySet> findByCreatorId(Long id);
    List<StudySet> findByCreatorIdAndIsActiveTrue(Long id);

//    Page<StudySet> findByTagContainsAndIsPublicTrue(String tag, Pageable pageable);
    Page<StudySet> findByTagContainsAndIsPublicTrueAndIsActiveTrue(String tag, Pageable pageable);

//    List<StudySet> findTop6ByCreatorIdOrderByIdDesc(Long id);
    List<StudySet> findTop6ByIsActiveTrueAndCreatorIdOrderByIdDesc(Long id);

//    @Query(value = "SELECT creator_id FROM study_set where id = ?1 ", nativeQuery = true)
    @Query(value = "SELECT creator_id FROM study_set where is_active = true AND id = ?1 ", nativeQuery = true)
    Long findCreatorIdById(Long studySetId);


//    @Query(value = "select * from study_set where id in (  select distinct report.study_set_id from report )", nativeQuery = true)
    @Query(value = "select * from study_set where is_active = true and id in (  select distinct report.study_set_id from report )", nativeQuery = true)
    List<StudySet> findAllSSHasReport();

    StudySet findByIdAndIsActiveTrue(Long id);

    Optional<StudySet> findById(Long id);


}
