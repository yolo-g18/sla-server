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

    @GetMapping(value = "event")
    public List<EventDto> showEvent(@RequestParam("from") String from,@RequestParam("to") String to){
        return eventService.getAllBetweenDates(from,to);
    }

    @PostMapping(value = "event")
    public EventDto createEvent(@RequestBody EventDto eventDto) {
       return eventService.save(eventDto);
    }

    @PutMapping(value = "event/{id}")
    public EventDto updateEvent(@RequestBody EventDto eventDto,@PathVariable("id") long id) {
        eventDto.setId(id);
        //need to check event is null before save
        return eventService.save(eventDto);
    }

    @DeleteMapping(value = "event")
    public void deleteEvent ( @RequestBody long[] ids){
        eventService.delete(ids);
    }


}
