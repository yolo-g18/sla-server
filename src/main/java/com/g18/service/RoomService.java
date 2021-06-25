package com.g18.service;

import com.g18.entity.*;
import com.g18.repository.RoomRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PreRemove;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Room saveRoom(Room room){
        return roomRepository.save(room);
    }

    public List<Room> getRoomList(){
        return roomRepository.findAll();
    }

    public Room getRoomByID(Long id){
        return roomRepository.findById(id).orElse(null);
    }

    public String deleteRoom(Long id){
        roomRepository.deleteById(id);
        return "remove room successfully";
    }

    public Room editRoom(Room room){
        Room existingRoom = roomRepository.findById(room.getId()).orElse(null);
        existingRoom.setName(room.getName());
        existingRoom.setDescription(room.getDescription());
        existingRoom.setUpdateDate(room.getUpdateDate());
        return roomRepository.save(existingRoom);
    }

    public Room addMember(RoomMember roomMember){
        Room existingRoom = roomRepository.findById(roomMember.getRoomMemberId().getRoomId()).orElse(null);
        existingRoom.getRoomMembers().add(roomMember);
        return roomRepository.save(existingRoom);
    }

    @Transactional
    public String deleteRelationshipRoomMember(Long room_id,Long member_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        RoomMember existingRoomMember =existingRoom.getRoomMembers().stream().filter(
                roomMember ->
                    roomMember.getRoomMemberId().getMemberId().equals(member_id) &&
                            roomMember.getRoomMemberId().getRoomId().equals(room_id)

        ).findAny().orElse(null);
        existingRoom.getRoomMembers().remove(existingRoomMember);
        return "remove RoomMember successfully";
    }

}
