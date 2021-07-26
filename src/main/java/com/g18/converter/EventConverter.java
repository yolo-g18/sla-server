package com.g18.converter;

import com.g18.dto.EventDto;
import com.g18.entity.Event;
import com.g18.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EventConverter {
    @Autowired
    private AuthService authService;
    public Event toEntity(EventDto eventDto){
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setFromTime(Instant.parse(eventDto.getFromTime()));
        event.setToTime(Instant.parse(eventDto.getToTime()));
        event.setColor(eventDto.getColor());
        event.setCreatedTime(Instant.parse(eventDto.getCreatedTime()));
        event.setUpdateTime(Instant.parse(eventDto.getUpdateTime()));
        event.setLearnEvent(eventDto.isLearnEvent());
        event.setUser(authService.getCurrentUser());
        return event;
    }

    public EventDto toDto(Event event){
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDescription(event.getDescription());
        eventDto.setFromTime(event.getFromTime().toString());
        eventDto.setToTime(event.getToTime().toString());
        eventDto.setColor(event.getColor());
        eventDto.setCreatedTime(event.getCreatedTime().toString());
        eventDto.setUpdateTime(event.getUpdateTime().toString());
        eventDto.setLearnEvent(event.isLearnEvent());
        eventDto.setUserId(event.getId());
        return eventDto;
    }

    public Event toEntity(EventDto eventDto,Event event){
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setFromTime(Instant.parse(eventDto.getFromTime()));
        event.setToTime(Instant.parse(eventDto.getToTime()));
        event.setColor(eventDto.getColor());
        event.setCreatedTime(Instant.parse(eventDto.getCreatedTime()));
        event.setUpdateTime(Instant.parse(eventDto.getUpdateTime()));
        event.setLearnEvent(eventDto.isLearnEvent());
        event.setUser(authService.getCurrentUser());
        return event;
    }


}
