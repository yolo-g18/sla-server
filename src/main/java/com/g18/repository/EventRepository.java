package com.g18.repository;

import com.g18.dto.EventDto;
import com.g18.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    void deleteById(long id);
  
    @Query(value = "SELECT * FROM event WHERE created_time BETWEEN :from AND :to", nativeQuery = true)
    List<Event> getAllBetweenDates(@Param("from") String from, @Param("to") String to);

    Page<Event> findByNameContaining(String name,Pageable pageable);


    @Query(value = "SELECT * FROM event WHERE user_id = :userId AND is_learn_event = 1 and to_time LIKE :date% ORDER BY to_time DESC",nativeQuery = true)

    List<Event> getListEventByUserIdAndDate(@Param("userId") Long userId, @Param("date") String  date);

    List<Event> findEventByToTimeBefore(Instant toTime);
}

