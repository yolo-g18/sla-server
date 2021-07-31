package com.g18.repository;

import com.g18.entity.StudySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface StudySetRepository extends JpaRepository<StudySet,Long> {
    Page<StudySet> findByTitleContainsAndIsPublicTrue(String title, Pageable pageable);

    List<StudySet> findByCreatorId(Long id);

    Page<StudySet> findByTagContainsAndIsPublicTrue(String tag, Pageable pageable);

    List<StudySet> findTop6ByCreatorIdOrderByIdDesc(Long id);

    @Query(value = "SELECT creator_id FROM sla_db.study_set where id = ?1 ", nativeQuery = true)
    Long findCreatorIdById(Long studySetId);
    @Query(value = "select * from sla_db.study_set where id in (  select distinct report.study_set_id from sla_db.report )", nativeQuery = true)
    List<StudySet> findAllSSHasReport();



}
