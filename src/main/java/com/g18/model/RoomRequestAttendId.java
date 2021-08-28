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
public class RoomRequestAttendId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "room_id")
    private Long roomId;
}
