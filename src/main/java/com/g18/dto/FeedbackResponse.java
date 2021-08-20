package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private String userName;
    private double rating;
    private String feedback;
    private String avatar;
}
