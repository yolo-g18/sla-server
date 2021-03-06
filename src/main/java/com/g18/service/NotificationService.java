package com.g18.service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.NotificationDto;
import com.g18.entity.Notification;
import com.g18.entity.User;
import com.g18.repository.NotificationRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthService authService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
            .withLocale( Locale.UK )
            .withZone( ZoneId.systemDefault() );


    @Transactional
    public String saveNotification(ObjectNode json){

        Long creator_id = null;
        // parsing id of person created folder
        try {
            creator_id = Long.parseLong(json.get("creator_id").asText());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        Notification notify = new Notification();

        //Set attributes
        notify.setTitle(json.get("title").asText());
        notify.setDescription(json.get("description").asText());
        notify.setType(json.get("type").asText());
        notify.setLink(json.get("link").asText());
        notify.setRead(Boolean.parseBoolean(json.get("isRead").asText()));

        User owner = userRepository.getOne(creator_id);
        notify.setUser(owner);
        notify.setCreatedTime(Instant.now());

        try{
            notify.setTimeTrigger(Instant.parse(json.get("timeTrigger").asText()));
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        notificationRepository.save(notify);
        return "create notification successfully";
    }

    public String deleteNotification(long[] ids) {
        for (long item: ids){
            notificationRepository.deleteById(item);
        }
        return "Remove notification successfully";
    }
    //get top 20 notifications and paging
    public Page<NotificationDto> getNotification(Long userId, Pageable pageable){

        Page<Notification> notiPage = notificationRepository.findByUserIdOrderByCreatedTimeDesc(userId,pageable);
        int totalElements = (int) notiPage.getTotalElements();
        return new PageImpl<NotificationDto>(
                notiPage.stream().map(notification -> new NotificationDto(
                                notification.getId(),notification.getTitle(),notification.getDescription(),
                                notification.getType(),notification.getLink(),formatter.format(notification.getCreatedTime()),
                                String.valueOf(notification.getTimeTrigger()),notification.isRead()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    @Transactional
    public void readNews(ObjectNode json)
    {
        Long notiId = null;
        Long userId = null;

        try{
            notiId = Long.parseLong(json.get("notiId").asText());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        try{
            userId = Long.parseLong(json.get("userId").asText());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


        notificationRepository.readNew(notiId,userId);
    }

    @Transactional
    public void readAllNews(ObjectNode json){

        Long userId = null;

        try{
            userId = Long.parseLong(json.get("userId").asText());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        notificationRepository.readAllNews(userId);
    }

    @Transactional
    public Long getNotReadNewsNumber(Long userId){
        return notificationRepository.getNotReadNewsNumber(userId);
    }


}
