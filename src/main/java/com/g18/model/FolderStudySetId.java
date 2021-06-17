package com.g18.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FolderStudySetId implements Serializable {
    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "study_set_id")
    private Long studySetId;
}
