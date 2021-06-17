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
public class UserStudySetId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "study_set_id")
    private Long studySetId;
}
