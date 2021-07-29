package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.g18.service.RoomService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/createRoom")
    public String createRoom(@RequestBody ObjectNode json){ return roomService.saveRoom(json);
    }

    @GetMapping("/getRoomListOfUser/{id}")
    public List<ObjectNode> findAllRooms(@PathVariable Long id){
        return roomService.getRoomListOfUser(id);
    }

    @GetMapping("/getRoom/{id}")
    public ObjectNode getRoomByID(@PathVariable Long id){
        return roomService.getRoomByID(id);
    }

    @PutMapping("/editRoom")
    public String editRoom(@RequestBody ObjectNode json){
        return roomService.editRoom(json);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id) {
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

    @GetMapping("/isCreatorOfRoom/{id}")
    public boolean isCreatorOfRoom(@PathVariable Long id){
        return roomService.isCreatorOfRoom(id);
    }

    @GetMapping("/isMemberOfRoom/{id}")
    public boolean isMemberOfRoom(@PathVariable Long id){
        return roomService.isMemberOfRoom(id);
    }

    @DeleteMapping("/removeAllMemberOfRoom/{id}")
    public String removeAllMember(@PathVariable Long id){
        return roomService.removeAllMemberRoom(id);
    }

    @PutMapping("/requestAttendRoom")
    public String requestAttendRoom(@RequestBody ObjectNode json){
        return  roomService.requestAttendRoom(json);}

    @PutMapping("/inviteUserToRoom")
    public String inviteUserToRoom(@RequestBody ObjectNode json){
        return  roomService.inviteUserToRoom(json);}

    @DeleteMapping("/deleteRoomInvitation/{room_id}/{user_id}")
    public String deleteRoomInvitation(@PathVariable Long room_id, @PathVariable Long user_id){
        return roomService.deleteRoomInvitation(room_id,user_id);
    }

    @DeleteMapping("/deleteRoomRequestAttend/{room_id}/{user_id}")
    public String deleteRoomRequestAttend(@PathVariable Long room_id, @PathVariable Long user_id){
        return roomService.deleteRoomRequestAttend(room_id,user_id);
    }

    @GetMapping("/listRoomInvitations/{id}")
    public List<ObjectNode> listInvitationsOfRoom(@PathVariable Long id){
        return roomService.getRoomInvitaionList(id);
    }

    @GetMapping("/listRoomRequestAttend/{id}")
    public List<ObjectNode> listRoomRequestAttend(@PathVariable Long id){
        return roomService.getRoomRequestAttendList(id);
    }
}
