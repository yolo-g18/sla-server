package com.g18.repository;

import com.g18.entity.FolderStudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderStudySetRepository extends JpaRepository<FolderStudySet,Long> {
    @Query(value = "Select distinct study_set_id from sla_db.folder_study_set where folder_id = ?1",nativeQuery = true)
    List<Long> findNumberSSID(long folderID);

}
