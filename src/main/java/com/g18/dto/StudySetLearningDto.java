package com.g18.dto;

import com.g18.entity.StudySet;
import com.g18.entity.User;
import com.g18.model.Color;
import com.g18.model.Status;
import com.g18.model.UserStudySetId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetLearningDto {

    private String creatorName;

    private Long studySetId;

    private double progress;

    private Status status;

    private double rating;
    private String feedback;

    private Color color;

    private String startDate;
    private String expectedDate;
    private boolean isPublic;
}