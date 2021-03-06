package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.exceptions.*;
import com.g18.model.*;
import com.g18.repository.FolderRepository;
import com.g18.repository.RoomRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Service
public class RoomService {


    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
            .withLocale( Locale.UK )
            .withZone( ZoneId.systemDefault());

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudySetService studySetService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private LearningService learningService;

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

        return "create Room successfully";
    }

    @Transactional
    public List<ObjectNode> getRoomListOfUser(Long id){

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

        // json load all rooms to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        List<Long> listRoomIdJoin = listRoomIdAttendOfUser(id);

        // load all room attend to json list
        for (Long item : listRoomIdJoin) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            Room room = roomRepository.getOne(item);
            json.put("room_id",room.getId());
            json.put("name",room.getName());
            json.put("ownerName",userService.getUserNameOfPerson(room.getOwner().getId()));
            json.put("numberOfMembers",room.getRoomMembers().size());
            json.put("createdDate", formatter.format(room.getCreatedDate()));
            objectNodeList.add(json);
        }

        return  objectNodeList;
    }


    public HashSet<Long> listIdOfSetsInRoom(Long id){

        HashSet<Long> hashSet = new HashSet<>();

        // find a specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // count set in room
        for (RoomStudySet roomStudySet : existingRoom.getRoomStudySets())
        {
            hashSet.add(roomStudySet.getRoomStudySetId().getStudySetId());
        }

        // count set in folder included in room
        for(RoomFolder roomFolder : existingRoom.getRoomFolders())
        {
            Folder folder = folderRepository.findById(roomFolder.getRoomFolderId().getFolderId()).
                    orElseThrow(() -> new FolderNotFoundException());

            for(FolderStudySet folderStudySet : folder.getFolderStudySets()){
                hashSet.add(folderStudySet.getFolderStudySetId().getStudySetId());
            }
        }

        return hashSet;
    }

    @Transactional
    public ObjectNode getRoomByID(Long id){

        // find a specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // create a json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("room_id",existingRoom.getId());
        json.put("name",existingRoom.getName());
        json.put("description",existingRoom.getDescription());
        json.put("createdDate", formatter.format(existingRoom.getCreatedDate()));
        json.put("ownerId",existingRoom.getOwner().getId());
        json.put("ownerName",userService.getUserNameOfPerson(existingRoom.getOwner().getId()));
        json.put("setNumbers",listIdOfSetsInRoom(id).size());
        json.put("folderNumbers",existingRoom.getRoomFolders().size());
        json.put ("memberNumbers", existingRoom.getRoomMembers().size());
        return json;

    }

    @Transactional
    public String removeAllMemberRoom(Long id){

        // verify room's permisson
        if(isCreatorOfRoom(id) == false)
            throw new RoomPermisson();

        // find that specific room
        Room room = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        roomRepository.removeAllMember(id);

        return "remove all members successfully";
    }


    @Transactional
    public String deleteRoom(Long id){

        // verify room's permisson
        if(isCreatorOfRoom(id) == false)
            throw new RoomPermisson();

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

        // verify room's permisson
        if(isCreatorOfRoom(id) == false)
            throw new RoomPermisson();

        // find that specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // update attributes
        existingRoom.setName(json.get("name").asText());
        existingRoom.setDescription(json.get("description").asText());
        existingRoom.setUpdateDate(Instant.now());

        roomRepository.save(existingRoom);

        return "edit Room successfully";
    }

    @Transactional
    public String requestAttendRoom(ObjectNode json){

        Long room_id = null,user_id = null;

        // parsing id of room
        try {

            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // parsing id of person
        try {

            user_id = Long.parseLong(json.get("user_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that user
        User existingUser = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException());

        //create id of roomRequestAttend
        RoomRequestAttendId roomRequestAttendId = new RoomRequestAttendId();

        roomRequestAttendId.setRoomId(room_id);
        roomRequestAttendId.setUserId(user_id);

        RoomRequestAttend roomRequestAttend = new RoomRequestAttend();

        // set attributes form roomRequestAttend
        roomRequestAttend.setRoomRequestAttendId(roomRequestAttendId);
        roomRequestAttend.setUser(existingUser);
        roomRequestAttend.setRoom(existingRoom);
        roomRequestAttend.setRequestedDate(Instant.now());

        // add relationship roomRequestAttend
        existingRoom.getRoomRequestAttends().add(roomRequestAttend);

        roomRepository.saveAndFlush(existingRoom);

        return "send request successfully";
    }

    @Transactional
    public String inviteUserToRoom(ObjectNode json){

        Long room_id = null,user_id = null;

        // parsing id of room
        try {

            room_id = Long.parseLong(json.get("room_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // verify room's permisson
        if(isCreatorOfRoom(room_id) == false)
            throw new RoomPermisson();

        // parsing id of person
        try {

            user_id = Long.parseLong(json.get("user_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that user
        User existingUser = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException());

        // check for user exist in invitation of room
        Long finalUser_id = user_id;
        Long finalRoom_id = room_id;
        RoomInvitation temp = existingRoom.getRoomInvitations().stream().filter(
                roomInvitation -> roomInvitation.getUser().getId().equals(finalUser_id)
                        && roomInvitation.getRoom().getId().equals(finalRoom_id)
        ).findAny().orElse(null);

        // invexisted
        if(null != temp)
            throw new SLAException("invitation has been sent!");

        //create id of roomInvitationId
        RoomInvitationId roomInvitationId = new RoomInvitationId();

        roomInvitationId.setRoomId(room_id);
        roomInvitationId.setUserId(user_id);

        RoomInvitation roomInvitation = new RoomInvitation();

        // set attributes form roomInvitation
        roomInvitation.setRoomInvitationId(roomInvitationId);
        roomInvitation.setUser(existingUser);
        roomInvitation.setRoom(existingRoom);
        roomInvitation.setInvitedDate(Instant.now());

        // add relationship roomInvitation
        existingRoom.getRoomInvitations().add(roomInvitation);

        roomRepository.saveAndFlush(existingRoom);

        return "invite user successfully";
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
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that member
        User existingMember = userRepository.findById(member_id).orElseThrow(() -> new UserNotFoundException());

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

        // provide learning permisson for member
        for(Long id: listIdOfSetsInRoom(room_id))
        {
            StudySet studySet = studySetRepository.findById(id).orElseThrow(() -> new StudySetNotFoundException());
            learningService.addStudySetLearningByUserAndStudySet(existingMember,studySet);
        }

        roomRepository.saveAndFlush(existingRoom);

        return "add Member to Room successfully";
    }

    @Transactional
    public String deleteRoomInvitation(Long room_id,Long user_id){

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that member
        User existingMember = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException());

        // find roomInvitation in roomInvitationList of a room
        RoomInvitation roomInvitation =existingRoom.getRoomInvitations().stream().filter(
                room_Invitation ->
                        room_Invitation.getRoomInvitationId().getUserId().equals(user_id) &&
                                room_Invitation.getRoomInvitationId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        // remove relationship roomIvitation
        existingRoom.getRoomInvitations().remove(roomInvitation);

        roomRepository.saveAndFlush(existingRoom);

        return "remove roomInvitation successfully";
    }

    @Transactional
    public String deleteRoomRequestAttend(Long room_id,Long user_id){

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that member
        User existingMember = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException());

        // find roomRequestAttend in roomRequestAttendList of a room
        RoomRequestAttend roomRequestAttend = existingRoom.getRoomRequestAttends().stream().filter(
                requestAttend ->
                        requestAttend.getRoomRequestAttendId().getUserId().equals(user_id) &&
                                requestAttend.getRoomRequestAttendId().getRoomId().equals(room_id)

        ).findAny().orElse(null);

        // remove relationship roomRequestAttend
        existingRoom.getRoomRequestAttends().remove(roomRequestAttend);

        roomRepository.saveAndFlush(existingRoom);

        return "remove roomRequestAttend successfully";
    }

    @Transactional
    public String deleteMemberFromRoom(Long room_id,Long member_id){

        // find that room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());
        // find that member
        User existingMember = userRepository.findById(member_id).orElseThrow(() -> new UserNotFoundException());

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

        // verify room's permisson
        if(isCreatorOfRoom(room_id) == false)
            throw new RoomPermisson();

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
        Folder folder = folderRepository.findById(folder_id).orElseThrow(() -> new FolderNotFoundException());
        // find specific room
        Room room = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());

        // check for SS exist in room
        Long finalFolder_id = folder_id;
        Long finalRoom_id = room_id;
        RoomFolder temp = room.getRoomFolders().stream().filter(
                roomFolder -> roomFolder.getRoomFolderId().getFolderId().equals(finalFolder_id)
                        && roomFolder.getRoomFolderId().getRoomId().equals(finalRoom_id)
        ).findAny().orElse(null);

        // folder existed in room
        if(null != temp)
            throw new SLAException("folder existed in room");

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

        // verify room's permisson
        if(isCreatorOfRoom(room_id) == false)
            throw new RoomPermisson();

        // find specific folder
        Folder folder = folderRepository.findById(folder_id).orElseThrow(() -> new FolderNotFoundException());
        // find specific room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());

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

        // verify room's permisson
        if(isCreatorOfRoom(room_id) == false)
            throw new RoomPermisson();


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

        // find specific room
        Room room = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());

        // find specific studySet
        StudySet studySet = studySetRepository.findById(studySet_id).orElseThrow(() -> new StudySetNotFoundException());

        // check for SS exist in room
        Long finalStudySet_id = studySet_id;
        Long finalRoom_id = room_id;
        RoomStudySet temp = room.getRoomStudySets().stream().filter(
                roomStudySet -> roomStudySet.getRoomStudySetId().getStudySetId().equals(finalStudySet_id)
                        && roomStudySet.getRoomStudySetId().getRoomId().equals(finalRoom_id)
        ).findAny().orElse(null);


        if(null != temp)
            throw new SLAException("set existed in room");

        RoomStudySet roomStudySet = new RoomStudySet();

        // set attributes for roomStudySet
        roomStudySet.setRoomStudySetId(roomStudySetId);
        roomStudySet.setStudySet(studySet);
        roomStudySet.setRoom(room);
        roomStudySet.setCreatedDate(Instant.now());

        // add relationship roomStudySet
        room.getRoomStudySets().add(roomStudySet);

        // provide learning set permisson
        for(RoomMember roomMember : room.getRoomMembers())
        {
            learningService.addStudySetLearningByUserAndStudySet(roomMember.getMember(),studySet);
        }

        roomRepository.saveAndFlush(room);

        return "add StudySet to Room successfully";
    }

    @Transactional
    public String deleteStudySetFromRoom(Long room_id,Long studySet_id){

        // verify room's permisson
        if(isCreatorOfRoom(room_id) == false)
            throw new RoomPermisson();

        // find specific room
        Room existingRoom = roomRepository.findById(room_id).orElseThrow(() -> new RoomNotFoundException());

        // find specific studySet
        StudySet studySet = studySetRepository.findById(studySet_id).orElseThrow(() -> new StudySetNotFoundException());

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

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // verify room's permisson
        if(isMemberOfRoom(id) == false)
            throw new RoomPermisson();

        // load all roomMembers in database
        List<RoomMember> roomMemberList = existingRoom.getRoomMembers();

        // json load all roomMembers to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(roomMemberList.isEmpty()){
            return objectNodeList;
        }

        // helper create objectnode
        ObjectMapper mapper;

        // load all roomMembers to json list
        for (RoomMember roomMember: roomMemberList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("member_id",roomMember.getRoomMemberId().getMemberId());
            json.put("userName",userService.getUserNameOfPerson(roomMember.getMember().getId()));
            json.put("avatar", roomMember.getMember().getAvatar());


            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomInvitationListOfUser(){

        // get user logined
        User user = authService.getCurrentUser();

        // load all roomInvitation
        List<RoomInvitation> roomInvitationList = user.getInvitationList();

        // json load all to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(roomInvitationList.isEmpty()){
            return objectNodeList;
        }

        // helper create objectnode
        ObjectMapper mapper;

        // load all to json list
        for (RoomInvitation roomInvitation: roomInvitationList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("roomId",roomInvitation.getRoom().getId());
            json.put("roomName",roomInvitation.getRoom().getName());
            json.put("userNameHost",userService.getUserNameOfPerson(roomInvitation.getRoom()
                    .getOwner().getId()));
            json.put("timeInvited",formatter.format(roomInvitation.getInvitedDate()));

            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomRequestAttendList(Long id){

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // verify room's permisson
        if(isMemberOfRoom(id) == false)
            throw new RoomPermisson();

        // load all roomRequestAttend
        List<RoomRequestAttend> roomRequestAttendList = existingRoom.getRoomRequestAttends();

        // json load all to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(roomRequestAttendList.isEmpty()){
            return objectNodeList;
        }

        // helper create objectnode
        ObjectMapper mapper;

        // load all to json list
        for (RoomRequestAttend roomRequestAttend: roomRequestAttendList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("user_id",roomRequestAttend.getRoomRequestAttendId().getUserId());
            json.put("userName",userService.getUserNameOfPerson(roomRequestAttend.getUser().getId()));
            json.put("time",formatter.format(roomRequestAttend.getRequestedDate()));
            json.put("avatar", roomRequestAttend.getUser().getAvatar());

            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomFolderList(Long id){

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // verify room's permisson
        if(isMemberOfRoom(id) == false)
            throw new RoomPermisson();

        // load all roomFolders in database
        List<RoomFolder> roomFolderList = existingRoom.getRoomFolders();

        // json load all roomFolders to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(roomFolderList.isEmpty()){
           return objectNodeList;
        }

        // helper create objectnode
        ObjectMapper mapper;

        // load all roomFolders to json list
        for (RoomFolder roomFolder: roomFolderList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("folder_id",roomFolder.getRoomFolderId().getFolderId());
            json.put("title", roomFolder.getFolder().getTitle());
            json.put("color",roomFolder.getFolder().getColor().toString());
            json.put("numberOfSets",roomFolder.getFolder().getFolderStudySets().size());
            json.put("createdDate", formatter.format(roomFolder.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getRoomStudySetList(Long id){

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // verify room's permisson
        if(isMemberOfRoom(id) == false)
            throw new RoomPermisson();

        // load all roomStudySets in database
        List<RoomStudySet> roomStudySetList = existingRoom.getRoomStudySets();

        // json load all roomStudySets to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(roomStudySetList.isEmpty()){
           return objectNodeList;
        }
        // helper create objectnode
        ObjectMapper mapper;

        // load all roomStudySets to json list
        for (RoomStudySet roomStudySet: roomStudySetList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("studySet_id",roomStudySet.getRoomStudySetId().getStudySetId());
            json.put("title", roomStudySet.getStudySet().getTitle());
            json.put("tags",roomStudySet.getStudySet().getTag());
            json.put("description",roomStudySet.getStudySet().getDescription());
            json.put("createdDate", formatter.format(roomStudySet.getCreatedDate()));
            json.put("numberOfCards",roomStudySet.getStudySet().getCards().size());
            Long studySetOwner_id = roomStudySet.getStudySet().getCreator().getId();
            json.put("creatorName",userService.getUserNameOfPerson(studySetOwner_id));
            String color = studySetService.getColorOfStudySetLearning(
                    roomStudySet.getRoomStudySetId().getStudySetId());
            json.put("color",color);
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    public boolean isCreatorOfRoom(Long id){

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // get creator of room
        User creatorOfRoom = existingRoom.getOwner();

        // get user logined
        User currenUserLogined = authService.getCurrentUser();

        // verify user is creator of room
        if(currenUserLogined.getId().equals(creatorOfRoom.getId()))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isMemberOfRoom(Long id){

        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // get user logined
        User currenUserLogined = authService.getCurrentUser();

        // get creator of room
        User creatorOfRoom = existingRoom.getOwner();

        // firstly, verify user is creator of room (creator also a member of room)
        if(currenUserLogined.getId().equals(creatorOfRoom.getId()))
        {
            return true;
        }

        // secondly, determine user is member
        RoomMember roomMember = existingRoom.getRoomMembers().stream().filter(
                member -> member.getRoomMemberId().getMemberId().equals(currenUserLogined.getId())
        ).findAny().orElse(null);

        // verify user is member of room
        if(null == roomMember)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isUserRequestPending(Long id){
        // find specific room
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());

        // get user logined
        User currenUserLogined = authService.getCurrentUser();

        List<RoomRequestAttend> roomRequestAttendList = existingRoom.getRoomRequestAttends();

        RoomRequestAttend pending = roomRequestAttendList.stream().filter(
                requestAttend -> requestAttend.getUser().getId().equals(currenUserLogined.getId())
        ).findAny().orElse(null);

        if(null != pending)
             return true;

        return false;
    }

    @Transactional
    public Long getMaxId(){
        return roomRepository.getMaxId();
    }

    @Transactional
    public List<Long> listRoomIdAttendOfUser(Long userId){
        return roomRepository.listRoomIdAttendOfUser(userId);
    }


}
