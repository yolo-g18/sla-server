package com.g18.repository;
import com.g18.entity.Event;
import com.g18.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
     @Query(value = "SELECT * FROM notification where user_id = :userID order by created_time ASC LIMIT :limit", nativeQuery = true)
     //SELECT * FROM sla_db.notification where user_id = 1 order by created_time ASC LIMIT 2;
     List<Notification> findTop20ByUserIdOrderByCreatedTimeDesc(@Param("userID") Long userId,
                                                                @Param("limit") int limit);

     Page<Notification> findByUserIdOrderByCreatedTimeDesc(Long userID, Pageable pageable);

     @Modifying
     @Query(value = "update notification set is_read = 1 where id =:notiId and user_id=:userId", nativeQuery = true)
     public void readNew(@Param("notiId") Long notiId,@Param("userId") Long userId);

     @Modifying
     @Query(value =
             "update notification set is_read = 1 where user_id=:userId"
            , nativeQuery = true)
     public void readAllNews(@Param("userId") Long userId);
}
