package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {

    private String username;

    private Long studySetId;

    private double progress;

    private Instant startDate;
}