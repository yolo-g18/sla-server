package com.g18.entity;

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
@Entity
public class StudySetLearning {

    @EmbeddedId
    private UserStudySetId userStudySetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_set_id")
    @MapsId("studySetId")
    private StudySet studySet;

    private double progress;

    @Enumerated(EnumType.STRING)
    private Status status;

    private double rating;
    private String feedback;

    @Enumerated(EnumType.STRING)
    private Color color;

    private Instant startDate;
    private Instant expectedDate;

    private boolean isPublic;
}
