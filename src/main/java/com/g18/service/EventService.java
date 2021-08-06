package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.EventDto;
import com.g18.entity.Event;
import com.g18.entity.User;
import com.g18.model.Color;
import com.g18.repository.EventRepository;
import com.g18.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthService authService;

    public String save(ObjectNode json) {
        User user = authService.getCurrentUser();
        Event event =  new Event();
        event.setUser(user);

        event.setName(json.get("name").asText());
        event.setDescription(json.get("description").asText());
        event.setFromTime(Instant.parse(json.get("fromTime").asText()));
        event.setToTime(Instant.parse(json.get("toTime").asText()));
        event.setLearnEvent(Boolean.parseBoolean(json.get("isLearnEvent").asText()));
        event.setColor(Color.valueOf(json.get("color").asText()));
        event.setCreatedTime(Instant.now());
        event.setUpdateTime(Instant.now());

        event = eventRepository.save(event);
        return "Create event successfully";
    }


    public void update(Long eventId,ObjectNode json) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(()
                ->  new ExpressionException("Lỗi ko tìm thấy")) ;
        oldEvent.setName(json.get("name").asText());
        oldEvent.setDescription(json.get("description").asText());
        oldEvent.setFromTime(Instant.parse(json.get("fromTime").asText()));
        oldEvent.setToTime(Instant.parse(json.get("toTime").asText()));
        oldEvent.setLearnEvent(Boolean.parseBoolean(json.get("isLearnEvent").asText()));
        oldEvent.setColor(Color.valueOf(json.get("color").asText()));
        oldEvent.setUpdateTime(Instant.now());
        User user = authService.getCurrentUser();
        eventRepository.save(oldEvent);
    }

    public void delete(Long eventId) {
            eventRepository.deleteById(eventId);
    }

    public List<EventDto> getAllBetweenDates(String from,String to) {
        List<EventDto> results = new ArrayList<>();
        List<Event> events = eventRepository.getAllBetweenDates(authService.getCurrentUser().getId(),from,to);
        for (Event event : events){
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getId());
            eventDto.setUserId(event.getUser().getId());
            eventDto.setName(event.getName());
            eventDto.setDescription(event.getDescription());
            eventDto.setLearnEvent(event.isLearnEvent());
            eventDto.setFromTime(String.valueOf(event.getFromTime()));
            eventDto.setToTime(String.valueOf(event.getToTime()));
            eventDto.setUpdateTime(String.valueOf(event.getUpdateTime()));
            eventDto.setCreatedTime(String.valueOf(event.getCreatedTime()));
            results.add(eventDto);
        }
        return results;
    }

}