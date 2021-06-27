package com.g18.controller;

import com.g18.dto.EventDto;
import com.g18.service.IS.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class EventController {
    @Autowired
    private IEventService eventService;

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
