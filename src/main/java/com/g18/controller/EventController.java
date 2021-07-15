package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.EventDto;
import com.g18.dto.SearchStudySetResponse;
import com.g18.entity.Event;
import com.g18.repository.EventRepository;
import com.g18.service.EventService;
import com.g18.service.IS.IEventService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EventController {
    @Autowired
    private IEventService eventService;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService es;

    //Get event in a range of time
    @GetMapping(value = "event")
    public List<EventDto> showEvent(@RequestParam("from") String from,@RequestParam("to") String to){
        return eventService.getAllBetweenDates(from,to);
    }


    //Create event
    @PostMapping(value = "event")
    public ResponseEntity<String> createEvent(@Valid @RequestBody EventDto eventDto) {
         eventService.save(eventDto);
         return new ResponseEntity<>("Create Event Successful", HttpStatus.CREATED);
    }

    //Update event
    @PutMapping(value = "event/{id}")
    public ResponseEntity<String> updateEvent(@RequestBody EventDto eventDto,@PathVariable("id") String id_raw) throws NotFoundException {
        try{
            long id = Long.parseLong(id_raw);
            eventDto.setId(id);
            eventService.update(eventDto);
            return new ResponseEntity<>("Update Event Successful", HttpStatus.OK);
        }catch (Exception e){
            throw new NotFoundException("Event not found");
        }

    }
    //Delete event
    @DeleteMapping(value = "event")
    public ResponseEntity<String> deleteEvent ( @RequestBody long[] ids){
        try {
            eventService.delete(ids);
        }catch (Exception e){
            return new ResponseEntity<>("Not found Event", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Delete Event Successful", HttpStatus.OK);

    }



}
