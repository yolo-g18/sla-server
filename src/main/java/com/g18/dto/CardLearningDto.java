package com.g18.dto;

import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardLearningDto {

    private Long userId;
    private Long cardId;
    private Long studySetId;

    private String front;
    private String back;

    private String hint;
    private Color color;

}
