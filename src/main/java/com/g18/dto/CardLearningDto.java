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

    public CardLearningDto(Long userId, Long cardId, Long studySetId, String front, String back, String hint, String color) {
        this.userId = userId;
        this.cardId = cardId;
        this.studySetId = studySetId;
        this.front = front;
        this.back = back;
        this.hint = hint;
        this.color = Color.valueOf(color);
    }
}
