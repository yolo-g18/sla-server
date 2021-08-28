package com.g18.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.g18.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT COUNT(id) FROM sla_db.report where study_set_id = ?1", nativeQuery = true)
    int numberOfReportSS(Long ssId);

    List<Report> findByStudySetId(Long ssId);
    Page<Report> findByContentContains(String content,Pageable pageable);
    Optional<Report>findByStudySetIdAndAndReporterId(Long studySetId, Long ReporterID);
    Page<Report> findByIsCheckedFalse(Pageable pageable);
    Page<Report> findByIsCheckedTrue(Pageable pageable);

}
