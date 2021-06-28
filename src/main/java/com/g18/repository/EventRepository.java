package com.g18.repository;

import com.g18.dto.EventDto;
import com.g18.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    void deleteById(long id);
    @Query(value = "SELECT * FROM sla_db.event WHERE created_time >= '2021-06-26 17:30:08.958825' AND created_time <= '2021-06-27 17:30:08.958825'", nativeQuery = true)
    List<Event> getAllBetweenDates();
//    List<Event> getAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

