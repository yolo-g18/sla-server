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

    @PutMapping("/addMember")
    public String addMember(@RequestBody ObjectNode json){
        return roomService.addMember(json);}

    @DeleteMapping("/deleteMember/{room_id}/{member_id}")
    public String deleteMember(@PathVariable Long room_id, @PathVariable Long member_id){
        return roomService.deleteRelationshipRoomMember(room_id,member_id);
    }

    @PutMapping("/addExistingFolder")
    public Room addExistingFolder(@RequestBody RoomFolder roomFolder){
        return  roomService.addExistingFolder(roomFolder);}


}
