package com.g18.dto;


import com.g18.model.Color;
import com.g18.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    private Long id;
    private Long studySet;
    private String front;
    private String back;

    private int q; //quality
    private double intervalTime;
    private double eFactor;
    private String hint;
    private Instant LearnedDate;
    private Status status;
    private String color;

}
