package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.model.RoomMemberId;
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

    public String saveRoom(Room room){

        roomRepository.save(room);
        return "add Room successfully";
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

    public String editRoom(Room room){
        Room existingRoom = roomRepository.findById(room.getId()).orElse(null);
        existingRoom.setName(room.getName());
        existingRoom.setDescription(room.getDescription());
        existingRoom.setUpdateDate(room.getUpdateDate());
        roomRepository.save(existingRoom);
        return "edit Room successfully";
    }

    public String addMember(ObjectNode json){
        Long room_id = null,member_id = null;
        try {
            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        try {

            member_id = Long.parseLong(json.get("member_id").asText());
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        User existingMember = userRepository.findById(member_id).orElse(null);
        RoomMemberId roomMemberId = new RoomMemberId();
        roomMemberId.setRoomId(room_id);
        roomMemberId.setMemberId(member_id);

        RoomMember roomMember = new RoomMember();
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setMember(existingMember);
        roomMember.setRoom(existingRoom);

        existingRoom.getRoomMembers().add(roomMember);

        roomRepository.save(existingRoom);
        return "add Member successfully";
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


    public Room addExistingFolder(RoomFolder roomFolder){
        Room existingRoom = roomRepository.findById(roomFolder.getRoomFolderId().getRoomId()).orElse(null);
        existingRoom.getRoomFolders().add(roomFolder);
        return roomRepository.save(existingRoom);
    }

}
