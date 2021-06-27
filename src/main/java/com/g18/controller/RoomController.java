package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public String createRoom(@RequestBody ObjectNode json){ return roomService.saveRoom(json);
    }

    @GetMapping("/listRooms")
    public List<ObjectNode> findAllRooms(){
        return roomService.getRoomList();
    }

    @GetMapping("/room/{id}")
    public ObjectNode findRoomByID(@PathVariable Long id){
        return roomService.getRoomByID(id);
    }

    @PutMapping("/editRoom")
    public String editRoom(@RequestBody ObjectNode json){
        return roomService.editRoom(json);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id){
        return roomService.deleteRoom(id);
    }

    @PutMapping("/addMemberToRoom")
    public String addMemberToRoom(@RequestBody ObjectNode json){
        return roomService.addMemberToRoom(json);}

    @DeleteMapping("/deleteMemberFromRoom/{room_id}/{member_id}")
    public String deleteMemberFromRoom(@PathVariable Long room_id, @PathVariable Long member_id){
        return roomService.deleteMemberFromRoom(room_id,member_id);
    }

    @PutMapping("/addFolderToRoom")
    public String addFolderToRoom(@RequestBody ObjectNode json){
        return  roomService.addFolderToRoom(json);}

    @DeleteMapping("/deleteFolderFromRoom/{room_id}/{folder_id}")
    public String deleteFolderFromRoom(@PathVariable Long room_id, @PathVariable Long folder_id){
        return roomService.deleteFolderFromRoom(room_id,folder_id);
    }

    @PutMapping("/addStudySetToRoom")
    public String addStudySetToRoom(@RequestBody ObjectNode json){
        return  roomService.addStudySetToRoom(json);}

    @DeleteMapping("/deleteStudySetFromRoom/{room_id}/{studySet_id}")
    public String deleteStudySetFromRoom(@PathVariable Long room_id, @PathVariable Long studySet_id){
        return roomService.deleteStudySetFromRoom(room_id,studySet_id);
    }

    @GetMapping("/listMembersOfRoom/{id}")
    public List<ObjectNode> listMembersOfRoom(@PathVariable Long id){
        return roomService.getRoomMemberList(id);
    }

    @GetMapping("/listFoldersOfRoom/{id}")
    public List<ObjectNode> listFoldersOfRoom(@PathVariable Long id){
        return roomService.getRoomFolderList(id);
    }

    @GetMapping("/listStudySetsOfRoom/{id}")
    public List<ObjectNode> listStudySetsOfRoom(@PathVariable Long id){
        return roomService.getRoomStudySetList(id);
    }
}
