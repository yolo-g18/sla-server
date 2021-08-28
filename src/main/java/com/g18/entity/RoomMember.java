package com.g18.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.g18.model.RoomMemberId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomMember {

    @EmbeddedId
    private RoomMemberId roomMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @MapsId("memberId")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @MapsId("roomId")
    private Room room;

    private Instant enrolledDate;
}
