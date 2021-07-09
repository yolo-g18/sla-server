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
    private Long creator;
    private String title;
    private String description;
    private String tag;

    @JsonProperty
    private boolean isPublic;
    private List<Card> cards;
}
