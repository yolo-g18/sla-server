package com.g18.repository;
import com.g18.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Page<Room> findByNameContaining(String name, Pageable pageable);


    //SELECT * FROM sla_db.folder where title like CONCAT('%',:title,'%') and creator_id = :creatorId
   // @Query(value = "SELECT * FROM sla_db.room where name CONCAT('%',:name,'%') and owner_id = :ownerId",nativeQuery = true)
    @Query(value = "SELECT * FROM sla_db.room where name like CONCAT('%',:name,'%') and owner_id = :ownerId",nativeQuery = true)
    List<Room> findByTitleAndOwnerId(@Param("ownerId")long ownerId, @Param("name")String name);
}