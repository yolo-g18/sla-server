package com.g18.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g18.model.Color;
import com.g18.validation.FromTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FromTime
public class EventDto {
    private Long id;
    private Long userId;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name must not be blank")
    @Size(min = 5, max = 100, message = "Event must be between 5 and 100 characters")
    private String name;
    @Size(max = 500, message = "Description must less than characters")
    private String description;
    //check fromtime > to time
    @JsonProperty
    private boolean isLearnEvent;
    private String fromTime;
    private String toTime;

    private Color color;
    private String createdTime;
    private String updateTime;

}
