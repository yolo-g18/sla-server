package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.EventDto;
import com.g18.service.EventService;
import com.g18.service.IS.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    private IEventService eventService;

    @Autowired
    private EventService es;


    @GetMapping(value = "test")
    public String count(){
        List<EventDto> a = eventService.findEventBetweenDate();
        String rs = a.size() + " count";
        return rs;
    }

    @GetMapping(value = "event")
    public List<EventDto> showEvent(){
        return eventService.findEventBetweenDate();
    }

    @PostMapping(value = "event")
    public EventDto createEvent(@RequestBody EventDto eventDto) {
       return eventService.save(eventDto);
    }

    @PutMapping(value = "event/{id}")
    public EventDto updateEvent(@RequestBody EventDto eventDto,@PathVariable("id") long id) {
        eventDto.setId(id);
        return eventService.save(eventDto);
    }

    @DeleteMapping(value = "event")
    public void deleteEvent ( @RequestBody long[] ids){
        eventService.delete(ids);
    }


}
