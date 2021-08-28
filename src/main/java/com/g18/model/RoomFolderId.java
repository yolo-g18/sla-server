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
public class RoomFolderId implements Serializable {

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "folder_id")
    private Long folderId;
}
