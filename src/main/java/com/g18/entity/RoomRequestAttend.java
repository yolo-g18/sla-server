package com.g18.entity;

import com.g18.model.RoomInvitationId;
import com.g18.model.RoomRequestAttendId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomRequestAttend {
    @EmbeddedId
    private RoomRequestAttendId roomRequestAttendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @MapsId("roomId")
    private Room room;

    private Instant requestedDate;
}
