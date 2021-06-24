package com.g18.service;

import com.g18.entity.*;
import com.g18.repository.RoomRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void createRoomMember(RoomMember roomMember){
        Room existingRoom = roomRepository.findById(roomMember.getRoomMemberId().getRoomId()).orElse(null);
        existingRoom.getRoomMembers().add(roomMember);
        roomRepository.save(existingRoom);
        User existingUser = userRepository.findById(roomMember.getRoomMemberId().getMemberId()).orElse(null);
        existingUser.getRoomsJoin().add(roomMember);
        userRepository.save(existingUser);

    }


    public boolean addExistingFolderToRoom(RoomFolder roomFolder, Long room_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        return existingRoom.getRoomFolders().add(roomFolder);
    }

    public boolean addExistingStudySetToRoom(RoomStudySet roomStudySet, Long room_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        return existingRoom.getRoomStudySets().add(roomStudySet);
    }

    public boolean deleteMember(Long roomMember_id, Long room_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        return existingRoom.getRoomMembers().removeIf(roomMember -> (roomMember.getRoomMemberId().equals(roomMember_id)));
    }

    public boolean deleteFolderFromRoom(Long roomFolder_id, Long room_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        return existingRoom.getRoomFolders().removeIf(roomFolder -> (roomFolder.getRoomFolderId().equals(roomFolder_id)));
    }

    public boolean deleteStudySetFromRoom(Long roomStudyset_id, Long room_id) {
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        return existingRoom.getRoomStudySets().removeIf(roomStudySet -> (roomStudySet.getRoomStudySetId().equals(roomStudyset_id)));
    }
}
