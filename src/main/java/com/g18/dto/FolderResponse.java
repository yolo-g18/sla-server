package com.g18.dto;

import com.g18.entity.StudySet;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {
    private String username;
    private Long id;
    private String title;
    private String description;
    private Instant createdDate;
    private Instant updateDate;
    private Color color;

    private int numberOfSets;

}
