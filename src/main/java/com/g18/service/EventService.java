package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.converter.EventConverter;
import com.g18.dto.EventDto;
import com.g18.entity.Event;
import com.g18.entity.User;
import com.g18.exceptions.AccountException;
import com.g18.model.Color;
import com.g18.repository.EventRepository;
import com.g18.repository.UserRepository;
import com.g18.service.IS.IEventService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventService implements IEventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventConverter eventConverter;

    @Autowired
    private AuthService authService;

    @Override
    public String save(EventDto eventDto) {
        User user = authService.getCurrentUser();
        Event event =  eventConverter.toEntity(eventDto);
        event.setUser(user);
        event = eventRepository.save(event);
        return "Create event successfully";
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Event oldEvent = eventRepository.findById(eventDto.getId()).orElseThrow(()
                ->  new ExpressionException("Lỗi ko tìm thấy")) ;
        Event newEvent = eventConverter.toEntity(eventDto,oldEvent);
        User user = authService.getCurrentUser();
        newEvent.setUser(user);
        newEvent = eventRepository.save(newEvent);
        return eventConverter.toDto(newEvent);
    }

    @Override
    public void delete(long[] ids) {
        for (long item: ids){
            eventRepository.deleteById(item);
        }
    }

    @Override
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