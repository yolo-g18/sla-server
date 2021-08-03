package com.g18.service.IS;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.EventDto;

import java.util.List;

public interface IEventService {
    String save(ObjectNode json);
    EventDto update(EventDto eventDto);
    void delete(long[] ids);
    List<EventDto> getAllBetweenDates(String from, String to);

}