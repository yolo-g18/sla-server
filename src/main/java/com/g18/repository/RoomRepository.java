package com.g18.repository;
import com.g18.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Page<Room> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM room where name like CONCAT('%',:name,'%') and owner_id = :ownerId",nativeQuery = true)
    List<Room> findByTitleAndOwnerId(@Param("ownerId")long ownerId, @Param("name")String name);

    @Query(value = "SELECT max(room.id) FROM room",nativeQuery = true)
    Long getMaxId();

    @Query(value = "SELECT distinct room_id FROM room_member\n" +
            "where member_id =:userId",nativeQuery = true)
    List<Long> listRoomIdAttendOfUser(@Param("userId")Long userId);

    @Modifying
    @Query(value = "delete FROM room_member\n" +
            "where\n" +
            "room_id =:roomId\n" +
            "and\n" +
            "member_id not in (\n" +
            "select owner_id from room\n" +
            "where room_id =:roomId\n" +
            ")",nativeQuery = true)
    public void removeAllMember(@Param("roomId")Long roomId);
}
