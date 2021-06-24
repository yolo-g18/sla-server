package com.g18.controller;

import com.g18.entity.Room;
import com.g18.entity.RoomFolder;
import com.g18.entity.RoomMember;
import com.g18.entity.RoomStudySet;
import com.g18.model.RoomMemberId;
import com.g18.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/createRoom")
    public Room createRoom(@RequestBody Room room){
        return roomService.saveRoom(room);
    }

    @GetMapping("/listRooms")
    public List<Room> findAllRooms(){
        return roomService.getRoomList();
    }

    @GetMapping("/room/{id}")
    public Room findRoomByID(@PathVariable Long id){
        return roomService.getRoomByID(id);
    }

    @PutMapping("/editRoom")
    public Room editRoom(@RequestBody Room room){
        return roomService.editRoom(room);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id){
        return roomService.deleteRoom(id);
    }

    @PostMapping("/createRoomMember")
    public void createRoomMember(@RequestBody RoomMember roomMember)
    {
        roomService.createRoomMember(roomMember);
    }



    @PostMapping("/addExistingFolderToRoom")
    public boolean addExistingFolderToRoom(@RequestBody RoomFolder roomFolder, @RequestBody Long room_id){return roomService.addExistingFolderToRoom(roomFolder,room_id);}

    @PostMapping("/addExistingStudySetToRoom")
    public boolean addExistingStudySetToRoom(@RequestBody RoomStudySet roomStudySet, @RequestBody Long room_id){return roomService.addExistingStudySetToRoom(roomStudySet,room_id);}

    @DeleteMapping("/deleteMember/{roomMember_id}/{room_id}")
    public boolean deleteMember(@PathVariable Long roomMember_id, @PathVariable Long room_id){return roomService.deleteMember(roomMember_id,room_id);}

    @DeleteMapping("/deleteFolderFromRoom/{roomFolder_id}/{room_id}")
    public boolean deleteFolderFromRoom(@PathVariable Long roomFolder_id, @PathVariable Long room_id){return roomService.deleteFolderFromRoom(roomFolder_id,room_id);}

    @DeleteMapping("/deleteStudySetFromRoom/{roomStudyset_id}/{room_id}")
    public boolean deleteStudySetFromRoom(@PathVariable Long roomStudyset_id, @PathVariable Long room_id){return roomService.deleteStudySetFromRoom(roomStudyset_id,room_id);}
}
