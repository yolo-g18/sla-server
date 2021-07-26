package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.NotificationDto;
import com.g18.entity.Folder;
import com.g18.entity.Notification;
import com.g18.entity.User;
import com.g18.model.Color;
import com.g18.repository.NotificationRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotiService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthService authService;
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
        notify.setTimeTrigger(Instant.parse(json.get("timeTrigger").asText()));
        notificationRepository.save(notify);
        return "create notification successfully";
    }

    public String deleteNotification(long[] ids) {
        for (long item: ids){
            notificationRepository.deleteById(item);
        }
        return "Remove notification successfully";
    }

    public List<NotificationDto> getTop20(){
        List<Notification> listNoti = notificationRepository.findTop20ByUserIdOrderByCreatedTimeDesc(1L);
        List<NotificationDto> listDto = new ArrayList<>();
        for (Notification noti : listNoti){
            NotificationDto notiDto = new NotificationDto();
            notiDto.setNotiId(noti.getId());
            notiDto.setTitle(noti.getTitle());
            noti.setDescription(noti.getDescription());
            listDto.add(notiDto);
        }
        return listDto;
    }


}
