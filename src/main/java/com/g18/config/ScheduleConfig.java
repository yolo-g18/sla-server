package com.g18.config;

import com.g18.entity.CardLearning;
import com.g18.entity.Event;
import com.g18.entity.Notification;
import com.g18.entity.User;
import com.g18.repository.*;
import com.g18.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private CardLearningRepository cardLearningRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")//Run at 1 A.M every day
    public void updateDaily() {

        //Get time
        Instant timeUpdate = Instant.now();

        //Get list Event to Update Time
        List<Event> eventList = eventRepository.findEventByIsLearnEventAndToTimeBefore(true, timeUpdate);
        if(!eventList.isEmpty()){
            Date myDate = Date.from(timeUpdate.minus(1,ChronoUnit.DAYS));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(myDate);

            Instant from = timeUpdate.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();

            Instant to = timeUpdate.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();

            for(Event event : eventList){
                User user = event.getUser();
                Long studySetId = Long.valueOf(event.getDescription().trim());

                List<CardLearning> cards= cardLearningRepository.getListCardLearningByStudySetIdAndUserIdAndDate(studySetId, user.getId(), date);
                if(!cards.isEmpty()){
                    event.setFromTime(from);
                    event.setToTime(to);
                    eventRepository.save(event);

                    Notification notification = new Notification();
                    notification.setUser(user);
                    notification.setDescription("You have new task '"+event.getName()+"'.");
                    notification.setType("daily");
                    notification.setLink("/schedule");
                    notification.setCreatedTime(Instant.now());
                    notification.setTimeTrigger(Instant.now());
                    notification.setRead(false);
                    notificationRepository.save(notification);
                }
            }
        }

        //Get list CardLearning to Update Time
        List<CardLearning> cardLearningList = cardLearningRepository.findCardLearningByLearnedDateBefore(timeUpdate);
        if (!cardLearningList.isEmpty()) {
            for (CardLearning cardLearning : cardLearningList) {
                cardLearning.setLearnedDate(timeUpdate.truncatedTo(ChronoUnit.HOURS));
                cardLearningRepository.save(cardLearning);
            }
        }
    }

    @Scheduled(cron = "0 0 6 * * *")//Run at 6 A.M every day
    public void sendEmailDaily() {

        Date myDate = Date.from(Instant.now());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(myDate);

        String type = "daily";
        List<Notification> notificationList = notificationRepository.findNotificationByTypeAndTimeTrigger(type, today);
        if(!notificationList.isEmpty()) {
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            String learnTime = formatter.format(myDate);
            //List userId to check if sent mail
            List<Long> userIdList = new ArrayList<>();
            for (Notification noti : notificationList) {
                boolean isExist = false;
                for(Long userId : userIdList){
                    if(userId == noti.getUser().getId()){
                        isExist = true;
                    }
                }
                if(!isExist){
                    String username = accountRepository.findUserNameByUserId(noti.getUser().getId());

                    String toEmail = noti.getUser().getEmail();
                    String body = "Hello " + username + "\n" +
                            "You have some events to attend today.\n" +
                            "Please participate fully and on time events on SLA.\n" +
                            "Have a good day!";
                    String subject = "Notice to learn on " + learnTime;
                    emailSenderService.sendSimpleEmail(toEmail, body, subject);

                    //If isExist = false, add to list
                    userIdList.add(noti.getUser().getId());
                }
            }
        }
    }

    @Scheduled(cron = "* */10 * * * *")//Run after 10 minutes.
    public void sendEmailNotiLearn() {
        Instant now = Instant.now();
        Instant after10Minutes = now.plus(10, ChronoUnit.MINUTES);


        String type = "learn";
        List<Notification> notificationList = notificationRepository.findByTypeAndTimeTriggerBetweenOrderByTimeTrigger(type,now, after10Minutes);
        if(!notificationList.isEmpty()) {
            for (Notification noti : notificationList) {
                Instant timeTrigger = noti.getTimeTrigger();

                Date myDate = Date.from(timeTrigger);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String learnTime = formatter.format(myDate);

                String username = accountRepository.findUserNameByUserId(noti.getUser().getId());

                String toEmail = noti.getUser().getEmail();
                String body = "Hello " + username + "\n" +
                        "Time to learn is " + learnTime + "\n" +
                        "Please go to SLA to learn.";
                String subject = "Time to learn";
                emailSenderService.sendSimpleEmail(toEmail, body, subject);

                //update timeTrigger: plus one day
                timeTrigger = timeTrigger.plus(1, ChronoUnit.DAYS);
                noti.setTimeTrigger(timeTrigger);
                notificationRepository.save(noti);
            }
        }
    }
}
