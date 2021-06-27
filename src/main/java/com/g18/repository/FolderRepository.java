package com.g18.repository;

import com.g18.entity.Folder;
import com.g18.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository


public interface FolderRepository extends JpaRepository<Folder,Long> {
    List<Folder> getFolderByOwner (User user);
//    Optional<Folder> getAllFolder ();
}
