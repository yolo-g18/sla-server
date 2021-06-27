package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.model.RoomFolderId;
import com.g18.model.RoomMemberId;
import com.g18.model.RoomStudySetId;
import com.g18.repository.FolderRepository;
import com.g18.repository.RoomRepository;
import com.g18.repository.StudySetRepository;
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

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
            .withLocale( Locale.UK )
            .withZone( ZoneId.systemDefault() );;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Transactional
    public String saveRoom(ObjectNode json){

        Long owner_id = null;
        // parsing id of person created room
        try {
            owner_id = Long.parseLong(json.get("owner_id").asText());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        Room room = new Room();

        // set attributes for a new room
        room.setName(json.get("name").asText());
        room.setDescription(json.get("description").asText());
        User owner = userRepository.findById(owner_id).orElse(null);
        room.setOwner(owner);
        room.setCreatedDate(Instant.now());

        roomRepository.save(room);

        return "add Room successfully";
    }

    @Transactional
    public List<ObjectNode> getRoomList(){

         // load all rooms in database
         List<Room> roomList = roomRepository.findAll();

         // json load all rooms to client
         List<ObjectNode> objectNodeList = new ArrayList<>();

         // helper create objectnode
         ObjectMapper mapper;

         // load all room to json list
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

    @Transactional
    public ObjectNode getRoomByID(Long id){

        Room existingRoom = roomRepository.findById(id).orElse(null);
        HashMap<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("name",existingRoom.getName());
        json.put("description",existingRoom.getDescription());
        json.put("createdDate", formatter.format(existingRoom.getCreatedDate()));
        return json;

    }

    @Transactional
    public String deleteRoom(Long id){
        roomRepository.deleteById(id);
        return "remove room successfully";
    }

    @Transactional
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

    @Transactional
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

        RoomMember existingRoomMember =existingRoom.getRoomMembers().stream().filter(
                roomMember ->
                    roomMember.getRoomMemberId().getMemberId().equals(member_id) &&
                            roomMember.getRoomMemberId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        existingRoom.getRoomMembers().remove(existingRoomMember);
        roomRepository.saveAndFlush(existingRoom);
        return "remove Member successfully";
    }

    @Transactional
    public String addFolderToRoom(ObjectNode json){
        Long room_id = null,folder_id = null;

        try {
            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        try {

            folder_id = Long.parseLong(json.get("folder_id").asText());
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        RoomFolderId roomFolderId = new RoomFolderId();
        roomFolderId.setFolderId(folder_id);
        roomFolderId.setRoomId(room_id);

        Folder folder = folderRepository.findById(folder_id).orElse(null);
        Room room = roomRepository.findById(room_id).orElse(null);

        RoomFolder roomFolder = new RoomFolder();

        roomFolder.setRoomFolderId(roomFolderId);
        roomFolder.setFolder(folder);
        roomFolder.setRoom(room);
        roomFolder.setCreatedDate(Instant.now());

        room.getRoomFolders().add(roomFolder);

        roomRepository.saveAndFlush(room);

        return "add Folder to Room successfully";
    }

    @Transactional
    public String deleteRelationshipRoomFolder(Long room_id,Long folder_id){
        Room existingRoom = roomRepository.getOne(room_id);

        RoomFolder existingRoomFolder = existingRoom.getRoomFolders().stream().filter(
                roomFolder ->
                        roomFolder.getRoomFolderId().getFolderId().equals(folder_id) &&
                                roomFolder.getRoomFolderId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        existingRoom.getRoomFolders().remove(existingRoomFolder);
        roomRepository.saveAndFlush(existingRoom);
        return "remove Folder from Room successfully";
    }

    @Transactional
    public String addStudySetToRoom(ObjectNode json){
        Long room_id = null,studySet_id = null;

        try {
            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        try {

            studySet_id = Long.parseLong(json.get("studySet_id").asText());
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        RoomStudySetId roomStudySetId = new RoomStudySetId();
        roomStudySetId.setStudySetId(studySet_id);
        roomStudySetId.setRoomId(room_id);

        StudySet studySet = studySetRepository.getOne(studySet_id);
        Room room = roomRepository.getOne(room_id);

        RoomStudySet roomStudySet = new RoomStudySet();

        roomStudySet.setRoomStudySetId(roomStudySetId);
        roomStudySet.setStudySet(studySet);
        roomStudySet.setRoom(room);
        roomStudySet.setCreatedDate(Instant.now());

        room.getRoomStudySets().add(roomStudySet);

        roomRepository.saveAndFlush(room);

        return "add StudySet to Room successfully";
    }

    @Transactional
    public String deleteRelationshipRoomStudySet(Long room_id,Long studySet_id){
        Room existingRoom = roomRepository.getOne(room_id);

        RoomStudySet existingRoomStudySet = existingRoom.getRoomStudySets().stream().filter(
                roomStudySet ->
                        roomStudySet.getRoomStudySetId().getStudySetId().equals(studySet_id) &&
                                roomStudySet.getRoomStudySetId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        existingRoom.getRoomStudySets().remove(existingRoomStudySet);
        roomRepository.saveAndFlush(existingRoom);
        return "remove StudySet from Room successfully";
    }

}
