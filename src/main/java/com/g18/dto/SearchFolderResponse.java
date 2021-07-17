package com.g18.dto;


import com.g18.entity.FolderStudySet;
import com.g18.entity.User;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFolderResponse {

    private Long id;
    private String ownerName;
    private String title;
    private String description;
    private Instant createdDate;
    private Instant updateDate;
    private Color color;
    private int numberOfStudySets; //list study sets in folder
}
