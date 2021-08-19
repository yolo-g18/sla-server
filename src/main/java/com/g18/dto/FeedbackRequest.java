package com.g18.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    @Min(0)
    @Max(5)
    private double rating;

    @Size(max = 250, message = "Feedback cannot exceed 250 character.")
    private String feedback;

    private long studySetId;
}
