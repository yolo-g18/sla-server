package com.g18.repository;
import com.g18.entity.Event;
import com.g18.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FolderRepository extends JpaRepository<Folder,Long> {
    Page<Folder> findByTitleContaining(String title, Pageable pageable);
    @Query(value = "SELECT * FROM sla_db.folder where title like CONCAT('%',:title,'%') and creator_id = :creatorId",nativeQuery = true)
    List<Folder> findByCreatorIdAndTitleContaining(@Param("creatorId")long creatorId,@Param("title")String title);



}
