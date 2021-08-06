package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.converter.EventConverter;
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
import org.springframework.transaction.annotation.Transactional;

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
    private EventConverter eventConverter;

    @Autowired
    private AuthService authService;

    @Transactional
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

    @Transactional
    public EventDto update(EventDto eventDto) {
        Event oldEvent = eventRepository.findById(eventDto.getId()).orElseThrow(()
                ->  new ExpressionException("Lỗi ko tìm thấy")) ;
        Event newEvent = eventConverter.toEntity(eventDto,oldEvent);
        User user = authService.getCurrentUser();
        newEvent.setUser(user);
        newEvent = eventRepository.save(newEvent);
        return eventConverter.toDto(newEvent);
    }

    @Transactional
    public void delete(long[] ids) {
        for (long item: ids){
            eventRepository.deleteById(item);
        }
    }

    @Transactional
    public List<EventDto> getAllBetweenDates(String from,String to) {
        List<EventDto> results = new ArrayList<>();
        List<Event> events = eventRepository.getAllBetweenDates(authService.getCurrentUser().getId(),from,to);
        for (Event item : events){
            EventDto eventDto = eventConverter.toDto(item);
            results.add(eventDto);
        }
        return results;
    }

}