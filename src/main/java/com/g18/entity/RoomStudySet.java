package com.g18.entity;

import com.g18.model.RoomStudySetId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomStudySet {

    @EmbeddedId
    private RoomStudySetId roomStudySetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @MapsId("roomId")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_set_id")
    @MapsId("studySetId")
    private StudySet studySet;

    private Instant createdDate;
}
