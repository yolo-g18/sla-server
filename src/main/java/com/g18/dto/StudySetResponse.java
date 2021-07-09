package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetResponse {

    private Long id;
    private String username;
    private String title;
    private String description;
    private String tag;
    private boolean isPublic;
    private Integer numberOfCard;
}
