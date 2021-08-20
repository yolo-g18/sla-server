package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningrResponseDto {
    private double progress;
    private List<CardLearningDto> listCardLearning;
}
