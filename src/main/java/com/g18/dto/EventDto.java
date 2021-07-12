package com.g18.dto;

import com.g18.entity.User;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class EventDto {
    private Long id;
    private Long userId;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Instant fromTime;
    private Instant toTime;
    private Color color;
    private Instant createdTime;
    private Instant updateTime;

    public EventDto(Long id, Long userId, String name, String description, Instant fromTime, Instant toTime, Color color, Instant createdTime, Instant updateTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.color = color;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
    }

}
