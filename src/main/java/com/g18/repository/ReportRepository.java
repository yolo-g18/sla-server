package com.g18.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.g18.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT COUNT(id) FROM sla_db.report where study_set_id = ?1", nativeQuery = true)
    int numberOfReportSS(Long ssId);

    List<Report> findByStudySetId(Long ssId);
    Page<Report> findByIsCheckedFalse(Pageable pageable);
    Page<Report> findByIsCheckedFalseAndContentContains(String content,Pageable pageable);


}
