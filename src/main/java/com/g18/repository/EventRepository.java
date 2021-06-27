package com.g18.repository;

import com.g18.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
    void deleteById(long id);
}

