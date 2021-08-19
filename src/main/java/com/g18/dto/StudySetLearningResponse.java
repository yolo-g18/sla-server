package com.g18.dto;
import com.g18.model.Color;
import com.g18.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetLearningResponse {
    private Long userID;
    private String owner;
    private String creatorAvatar;
    private Long studySetId;
    private String studySetName;
    private String ssDescription;
    private double progress;
    private Status status;
    private double rating;
    private Color color;
    private int numberOfCards;
}
