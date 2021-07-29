package com.g18.config;

import com.g18.entity.CardLearning;
import com.g18.entity.Event;
import com.g18.repository.CardLearningRepository;
import com.g18.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private CardLearningRepository cardLearningRepository;

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(cron = "0 0 1 * * *")//Run at 1 A.M every day
    public void showTime() {

        //Get time
        Instant timeUpdate = Instant.now();

        //Get list CardLearning to Update Time
        List<CardLearning> cardLearningList = cardLearningRepository.findCardLearningByLearnedDateBefore(timeUpdate);
        if (cardLearningList != null) {
            for (CardLearning cardLearning : cardLearningList) {
                cardLearning.setLearnedDate(timeUpdate.truncatedTo(ChronoUnit.HOURS));
                cardLearningRepository.save(cardLearning);
            }
        }

        //Get list Event to Update Time
        List<Event> eventList = eventRepository.findEventByToTimeBefore(timeUpdate);
        if(eventList != null){
            Instant from = timeUpdate.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();

            Instant to = timeUpdate.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();

            for(Event event : eventList){
                event.setFromTime(from);
                event.setToTime(to);
                eventRepository.save(event);
            }
        }
    }
}
