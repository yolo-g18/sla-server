package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.exceptions.NoDataFoundException;
import com.g18.exceptions.RoomNotFoundException;
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
        User owner = userRepository.getOne(owner_id);
        room.setOwner(owner);
        room.setCreatedDate(Instant.now());

        roomRepository.save(room);

        return "add Room successfully";
    }

    @Transactional
    public List<ObjectNode> getRoomList(){

         // load all rooms in database
         List<Room> roomList = roomRepository.findAll();

         if(roomList.isEmpty()){
             throw new NoDataFoundException(); // not found room
         }


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

        // find a specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // create a json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();


        json.put("name",existingRoom.getName());
        json.put("description",existingRoom.getDescription());
        json.put("createdDate", formatter.format(existingRoom.getCreatedDate()));

        return json;
    }

    @Transactional
    public String deleteRoom(Long id){

        // find a specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        roomRepository.deleteById(id);

        return "remove room successfully";
    }

    @Transactional
    public String editRoom(ObjectNode json){

        Long id= null;

        // parsing id of room need edit
        try {
            id = Long.parseLong(json.get("id").asText());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // find that specific room
        Room existingRoom = roomRepository.getOne(id);

        // update attributes
        existingRoom.setName(json.get("name").asText());
        existingRoom.setDescription(json.get("description").asText());
        existingRoom.setUpdateDate(Instant.now());

        roomRepository.save(existingRoom);

        return "edit Room successfully";
    }

    @Transactional
    public String addMemberToRoom(ObjectNode json){

        Long room_id = null,member_id = null;

        // parsing id of room
        try {

            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // parsing id of member
        try {

            member_id = Long.parseLong(json.get("member_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElse(null);
        // find that member
        User existingMember = userRepository.findById(member_id).orElse(null);

        //create id of roomMember
        RoomMemberId roomMemberId = new RoomMemberId();

        roomMemberId.setRoomId(room_id);
        roomMemberId.setMemberId(member_id);


        RoomMember roomMember = new RoomMember();

        // set attributes form roomMember
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setMember(existingMember);
        roomMember.setRoom(existingRoom);
        roomMember.setEnrolledDate(Instant.now());

        // add relationship roomMember
        existingRoom.getRoomMembers().add(roomMember);

        roomRepository.saveAndFlush(existingRoom);

        return "add Member to Room successfully";
    }

    @Transactional
    public String deleteMemberFromRoom(Long room_id,Long member_id){

        // find specific room
        Room existingRoom = roomRepository.findById(room_id).orElse(null);

        // find roomMember in roomMemberList of a room
        RoomMember existingRoomMember =existingRoom.getRoomMembers().stream().filter(
                roomMember ->
                    roomMember.getRoomMemberId().getMemberId().equals(member_id) &&
                            roomMember.getRoomMemberId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        // remove relationship roomMember
        existingRoom.getRoomMembers().remove(existingRoomMember);

        roomRepository.saveAndFlush(existingRoom);

        return "remove Member from Room successfully";
    }

    @Transactional
    public String addFolderToRoom(ObjectNode json){

        Long room_id = null,folder_id = null;

        // parsing id of room
        try {

            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // parsing id of folder
        try {

            folder_id = Long.parseLong(json.get("folder_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // create id of roomFolder
        RoomFolderId roomFolderId = new RoomFolderId();

        roomFolderId.setFolderId(folder_id);
        roomFolderId.setRoomId(room_id);

        // find specific folder
        Folder folder = folderRepository.findById(folder_id).orElse(null);
        // find specific room
        Room room = roomRepository.findById(room_id).orElse(null);

        RoomFolder roomFolder = new RoomFolder();

        // set attributes for roomFolder
        roomFolder.setRoomFolderId(roomFolderId);
        roomFolder.setFolder(folder);
        roomFolder.setRoom(room);
        roomFolder.setCreatedDate(Instant.now());

        // add relationship roomFolder
        room.getRoomFolders().add(roomFolder);

        roomRepository.saveAndFlush(room);

        return "add Folder to Room successfully";
    }

    @Transactional
    public String deleteFolderFromRoom(Long room_id,Long folder_id){

        // find specific room
        Room existingRoom = roomRepository.findById(room_id).orElse(null);

        // find roomFolder in roomFolderList of room
        RoomFolder existingRoomFolder = existingRoom.getRoomFolders().stream().filter(
                roomFolder ->
                        roomFolder.getRoomFolderId().getFolderId().equals(folder_id) &&
                                roomFolder.getRoomFolderId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        // remove relationship roomFolder
        existingRoom.getRoomFolders().remove(existingRoomFolder);

        roomRepository.saveAndFlush(existingRoom);

        return "remove Folder from Room successfully";
    }

    @Transactional
    public String addStudySetToRoom(ObjectNode json){

        Long room_id = null,studySet_id = null;

        // parsing if of room
        try {

            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // parsing id of studySet
        try {

            studySet_id = Long.parseLong(json.get("studySet_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // create id of roomStudySet
        RoomStudySetId roomStudySetId = new RoomStudySetId();

        roomStudySetId.setStudySetId(studySet_id);
        roomStudySetId.setRoomId(room_id);

        // find specific studySet
        StudySet studySet = studySetRepository.findById(studySet_id).orElse(null);
        // find specific room
        Room room = roomRepository.findById(room_id).orElse(null);

        RoomStudySet roomStudySet = new RoomStudySet();

        // set attributes for roomStudySet
        roomStudySet.setRoomStudySetId(roomStudySetId);
        roomStudySet.setStudySet(studySet);
        roomStudySet.setRoom(room);
        roomStudySet.setCreatedDate(Instant.now());

        // add relationship roomStudySet
        room.getRoomStudySets().add(roomStudySet);

        roomRepository.saveAndFlush(room);

        return "add StudySet to Room successfully";
    }

    @Transactional
    public String deleteStudySetFromRoom(Long room_id,Long studySet_id){

        // find specific room
        Room existingRoom = roomRepository.findById(room_id).orElse(null);

        // find roomStudySet in roomStudySetList
        RoomStudySet existingRoomStudySet = existingRoom.getRoomStudySets().stream().filter(
                roomStudySet ->
                        roomStudySet.getRoomStudySetId().getStudySetId().equals(studySet_id) &&
                                roomStudySet.getRoomStudySetId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        // remove relationship roomStudySet
        existingRoom.getRoomStudySets().remove(existingRoomStudySet);

        roomRepository.saveAndFlush(existingRoom);

        return "remove StudySet from Room successfully";
    }

    @Transactional
    public List<ObjectNode> getRoomMemberList(Long id){

        // load all roomMembers in database
        List<RoomMember> roomMemberList = roomRepository.findById(id).get().getRoomMembers();

        // json load all roomMembers to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        // load all roomMembers to json list
        for (RoomMember roomMember: roomMemberList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("name",roomMember.getMember().getFirstName()+" "
                    +roomMember.getMember().getLastName());
            json.put("enrolledDate", formatter.format(roomMember.getEnrolledDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomFolderList(Long id){

        // load all roomFolders in database
        List<RoomFolder> roomFolderList = roomRepository.findById(id).get().getRoomFolders();

        // json load all roomFolders to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        // load all roomFolders to json list
        for (RoomFolder roomFolder: roomFolderList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("title", roomFolder.getFolder().getTitle());
            json.put("description",roomFolder.getFolder().getDescription());
            json.put("createdDate", formatter.format(roomFolder.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomStudySetList(Long id){

        // load all roomStudySets in database
        List<RoomStudySet> roomStudySetList = roomRepository.findById(id).get().getRoomStudySets();

        // json load all roomStudySets to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        // load all roomStudySets to json list
        for (RoomStudySet roomStudySet: roomStudySetList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("title", roomStudySet.getStudySet().getTitle());
            json.put("description",roomStudySet.getStudySet().getDescription());
            json.put("createdDate", formatter.format(roomStudySet.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }
}
