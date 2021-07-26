package com.g18.repository;

import com.g18.entity.FolderStudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderStudySetRepository extends JpaRepository<FolderStudySet,Long> {
    @Modifying
    @Query(value = "Select distinct study_set_id from sla_db.folder_study_set where :status",nativeQuery = true)
    List<Long> findNumberSSID(@Param("status") String status);

}
