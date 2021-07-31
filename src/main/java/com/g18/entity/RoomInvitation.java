package com.g18.entity;

import com.g18.model.RoomFolderId;
import com.g18.model.RoomInvitationId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomInvitation {
    @EmbeddedId
    private RoomInvitationId roomInvitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @MapsId("roomId")
    private Room room;

    private Instant invitedDate;
}
