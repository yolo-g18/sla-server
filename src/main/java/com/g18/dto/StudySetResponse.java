package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetResponse {

    private Long studySetId;
    private String creatorName;
    private Long userId;
    private String title;
    private String description;
    private String tag;
    private boolean isPublic;
    private Integer numberOfCard;

}
