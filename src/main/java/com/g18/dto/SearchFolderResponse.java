package com.g18.dto;


import com.g18.entity.FolderStudySet;
import com.g18.entity.User;
import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFolderResponse {

    private Long id;
    private String ownerName;
    @NotBlank(message = "Folder's title is required")
    @NotNull(message = "Folder's title is required")
    private String title;
    private String description;
    private Instant createdDate;
    private Instant updateDate;
    private Color color;
    private int numberOfStudySets; //list study sets in folder
}
