package com.g18.repository;
import com.g18.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
     List<Notification> findTop20ByUserIdOrderByCreatedTimeDesc(Long userId);

}
