package com.g18.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    @Min(0)
    @Max(5)
    private double rating;

    private String feedback;

    private long studySetId;
}
