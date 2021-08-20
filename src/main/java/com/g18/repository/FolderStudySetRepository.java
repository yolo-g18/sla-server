package com.g18.repository;

import com.g18.entity.FolderStudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderStudySetRepository extends JpaRepository<FolderStudySet,Long> {

    @Query(value = "Select distinct study_set_id from folder_study_set where folder_id = ?1",nativeQuery = true)
    List<Long> findNumberSSID(long folderID);

    @Modifying
    @Query(value = "Select distinct study_set_id from folder_study_set where :status",nativeQuery = true)
    List<Long> findNumberSSID(@Param("status") String status);


}
