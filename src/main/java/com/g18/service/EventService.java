package com.g18.service;

import com.g18.converter.EventConverter;
import com.g18.dto.EventDto;
import com.g18.entity.Event;
import com.g18.entity.User;
import com.g18.exceptions.AccountException;
import com.g18.repository.EventRepository;
import com.g18.repository.UserRepository;
import com.g18.service.IS.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

@Service
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


}