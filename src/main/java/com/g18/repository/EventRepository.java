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
    @Query(value = "SELECT * FROM sla_db.event WHERE created_time >= :startDate AND created_time <= :endDate", nativeQuery = true)
    List<Event> getAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

