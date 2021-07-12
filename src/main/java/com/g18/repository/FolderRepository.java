package com.g18.repository;
import com.g18.entity.Event;
import com.g18.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface FolderRepository extends JpaRepository<Folder,Long> {
    Page<Folder> findByTitleContaining(String title, Pageable pageable);

}
