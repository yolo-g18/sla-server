package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.converter.EventConverter;
import com.g18.dto.EventDto;
import com.g18.entity.Event;
import com.g18.entity.User;
import com.g18.exceptions.AccountException;
import com.g18.repository.EventRepository;
import com.g18.repository.UserRepository;
import com.g18.service.IS.IEventService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public EventDto save(EventDto eventDto) {
        User user = authService.getCurrentUser();
        Event event =  eventConverter.toEntity(eventDto);
        event.setUser(user);
        event = eventRepository.save(event);
        return eventConverter.toDto(event);
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
    public List<EventDto> findEventBetweenDate() {
        List<EventDto> results = new ArrayList<>();
        List<Event> events = eventRepository.findAll();
        for (Event item : events) {
            EventDto eventDto = eventConverter.toDto(item);
            results.add(eventDto);
            log.error(eventDto.getName() + " /| ");
        }
        return results;
    }

    @Override
    public List<EventDto> getAllBetweenDates() {
        List<EventDto> results = new ArrayList<>();
        List<Event> events = eventRepository.getAllBetweenDates();
        for (Event item : events){
            EventDto eventDto = eventConverter.toDto(item);
            results.add(eventDto);
        }
        return results;
    }

//    @Transactional
//    public List<ObjectNode> findAllBetweenDate() {
////        LocalDateTime start = LocalDateTime.of(LocalDate.parse(from), LocalTime.of(0, 0, 0));
////        LocalDateTime end = LocalDateTime.of(LocalDate.parse(to), LocalTime.of(23, 59, 59));
//        //load all events between date in database
//        List<Event> eventList = eventRepository.findAll();
//        // json load all rooms to client
//        List<ObjectNode> objectNodeList = new ArrayList<>();
//        // helper create objectnode
//        ObjectMapper mapper;
//        // load all room to json list
//        for (Event event : eventList) {
//            mapper = new ObjectMapper();
//            ObjectNode json = mapper.createObjectNode();
//            json.put("id", event.getId());
//            json.put("name", event.getName());
//            json.put("color", event.getColor().toString());
//            objectNodeList.add(json);
//        }
//        return objectNodeList;
//    }


}