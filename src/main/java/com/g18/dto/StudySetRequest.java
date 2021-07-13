package com.g18.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g18.entity.Card;
import com.g18.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetRequest {

    private Long id;
    @NotBlank(message = "Title is required")
    @NotNull(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @NotNull(message = "Description is required")
    private String description;

    private String tag;

    @JsonProperty
    private boolean isPublic;

    @Size(min = 2, message = "Must have at least 2 cards")
    private List<Card> cards;
}
