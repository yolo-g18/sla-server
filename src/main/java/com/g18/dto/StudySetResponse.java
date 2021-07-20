package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetResponse {

    private Long studySetId;
    private String creatorName;
    private String title;
    private String description;
    private String tag;
    private boolean isPublic;
    private Integer numberOfCard;
    private double progress;
    private Instant createdDate;

}
