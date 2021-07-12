package com.g18.repository;

import com.g18.entity.Folder;
import com.g18.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Page<Room> findByNameContaining(String name, Pageable pageable);
}