package com.g18.repository;

import com.g18.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface FolderRepository extends JpaRepository<Folder,Long> {
}
