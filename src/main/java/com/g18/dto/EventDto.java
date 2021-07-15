package com.g18.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g18.entity.User;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private Long userId;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String description;
    //check fromtime > to time
    @JsonProperty
    private boolean isLearnEvent;
    private Instant fromTime;
    private Instant toTime;

    private Color color;
    private Instant createdTime;
    private Instant updateTime;

}
