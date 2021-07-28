package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.NotificationDto;
import com.g18.dto.SearchStudySetResponse;
import com.g18.entity.Notification;
import com.g18.entity.User;
import com.g18.service.AuthService;
import com.g18.service.EmailSenderService;
import com.g18.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("api/notify")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AuthService authService;



    @PostMapping("/create")
    public String createNoti(@RequestBody ObjectNode json){ return notificationService.saveNotification(json);}
    //Delete event
    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteEvent (@RequestBody long[] ids){
        try {
            notificationService.deleteNotification(ids);
        }catch (Exception e){
            return new ResponseEntity<>("Not found Event", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Delete Event Successful", HttpStatus.OK);

    }
    //Get top 20 notification
    @GetMapping("/get")
    Page<NotificationDto> getNoti(@PageableDefault(size = 2) Pageable pageable){
        User currentUser = authService.getCurrentUser();
        return notificationService.getNotification(currentUser.getId(),pageable);
    }

    @GetMapping("/sendEmail")
    String sendEmail() throws MessagingException {
        emailSenderService.sendEmailWithAttachment("congvinh9d@gmail.com",
                "Dear CV\n<h1>Test send email</h1>",
                "This email has attachment test again",
                "C:\\Users\\congv\\OneDrive\\Máy tính\\Capture.PNG");
        return "sent email";
    }

}
