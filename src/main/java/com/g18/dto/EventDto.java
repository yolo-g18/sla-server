package com.g18.dto;

import com.g18.entity.User;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Instant fromTime;
    private Instant toTime;
    private Color color;
    private Instant createdTime;
    private Instant updateTime;

}
