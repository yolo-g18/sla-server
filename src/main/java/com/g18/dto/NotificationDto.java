package com.g18.dto;


import com.g18.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Long id;
    private String title;
    private String description;
    private String type;
    private String link;
    private String createdTime;
    private String timeTrigger;
    private boolean isRead;
}
