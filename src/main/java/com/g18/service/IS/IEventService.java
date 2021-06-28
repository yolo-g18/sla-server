package com.g18.service.IS;

import com.g18.dto.EventDto;

import java.util.List;

public interface IEventService {
    EventDto save(EventDto eventDto);
    EventDto update(EventDto eventDto);
    void delete(long[] ids);
    List<EventDto> getAllBetweenDates(String from, String to);

}