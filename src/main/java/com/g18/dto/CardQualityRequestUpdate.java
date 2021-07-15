package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardQualityRequestUpdate {

    private Long cardId;

    @Min(value=0, message="q must be greater than 0")
    @Max(value=5, message="q must be less than 0")
    private int q;
}
