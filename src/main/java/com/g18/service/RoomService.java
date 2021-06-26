package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.model.RoomMemberId;
import com.g18.repository.RoomRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PreRemove;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class RoomService {

    private DateTimeFormatter formatter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public String saveRoom(ObjectNode json){

        Long owner_id = null;

        try {
            owner_id = Long.parseLong(json.get("owner_id").asText());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        Room room = new Room();

        room.setName(json.get("name").asText());
        room.setDescription(json.get("description").asText());
        User owner = userRepository.findById(owner_id).orElse(null);
        room.setOwner(owner);
        room.setCreatedDate(Instant.now());

        roomRepository.save(room);
        return "add Room successfully";
    }

    public List<ObjectNode> getRoomList(){
        formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );

         List<Room> roomList = roomRepository.findAll();
         List<ObjectNode> objectNodeList = new ArrayList<>();
         ObjectMapper mapper;

         for (Room room: roomList) {
             mapper =  new ObjectMapper();
             ObjectNode json = mapper.createObjectNode();
             json.put("name",room.getName());
             json.put("description",room.getDescription());
             json.put("createdDate", formatter.format(room.getCreatedDate()));
             objectNodeList.add(json);
        }

         return objectNodeList;
    }

    public ObjectNode getRoomByID(Long id){
        formatter =
        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.UK )
                .withZone( ZoneId.systemDefault() );

        Room existingRoom = roomRepository.findById(id).orElse(null);
        HashMap<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("name",existingRoom.getName());
        json.put("description",existingRoom.getDescription());
        json.put("createdDate", formatter.format(existingRoom.getCreatedDate()));
        return json;

    }

    public String deleteRoom(Long id){
        roomRepository.deleteById(id);
        return "remove room successfully";
    }

    public String editRoom(ObjectNode json){
        Long id= null;

        try {
            id = Long.parseLong(json.get("id").asText());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        Room existingRoom = roomRepository.findById(id).orElse(null);
        existingRoom.setName(json.get("name").asText());
        existingRoom.setDescription(json.get("description").asText());
        existingRoom.setUpdateDate(Instant.now());
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
        roomMember.setEnrolledDate(Instant.now());
        existingRoom.getRoomMembers().add(roomMember);

        roomRepository.save(existingRoom);
        return "add Member successfully";
    }

    @Transactional
    public String deleteRelationshipRoomMember(Long room_id,Long member_id){
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        List<RoomMember> roomMembers = existingRoom.getRoomMembers();
        RoomMember existingRoomMember =existingRoom.getRoomMembers().stream().filter(
                roomMember ->
                    roomMember.getRoomMemberId().getMemberId().equals(member_id) &&
                            roomMember.getRoomMemberId().getRoomId().equals(room_id)

        ).findAny().orElse(null);
        boolean count = existingRoom.getRoomMembers().remove(existingRoomMember);
        roomRepository.saveAndFlush(existingRoom);
        return "remove Member successfully";
    }


    public Room addExistingFolder(RoomFolder roomFolder){
        Room existingRoom = roomRepository.findById(roomFolder.getRoomFolderId().getRoomId()).orElse(null);
        existingRoom.getRoomFolders().add(roomFolder);
        return roomRepository.save(existingRoom);
    }

}
