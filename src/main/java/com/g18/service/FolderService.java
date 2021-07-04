package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.exceptions.MemberNotFoundException;
import com.g18.exceptions.NoDataFoundException;
import com.g18.exceptions.RoomNotFoundException;
import com.g18.model.Color;
import com.g18.model.FolderStudySetId;
import com.g18.model.RoomMemberId;
import com.g18.repository.FolderRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FolderService {

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
            .withLocale( Locale.UK )
            .withZone( ZoneId.systemDefault() );

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public String saveFolder(ObjectNode json){

        Long creator_id = null;
        // parsing id of person created folder
        try {
            creator_id = Long.parseLong(json.get("creator_id").asText());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        Folder folder = new Folder();

        // set attributes for a new folder
        folder.setTitle(json.get("title").asText());
        folder.setDescription(json.get("description").asText());
        User owner = userRepository.getOne(creator_id);
        folder.setOwner(owner);
        folder.setCreatedDate(Instant.now());

        folderRepository.save(folder);

        return "create Folder successfully";
    }

    @Transactional
    public String editFolder(ObjectNode json){

        Long id= null;

        // parsing id of folder need edit
        try {
            id = Long.parseLong(json.get("id").asText());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // find that specific room
        Folder existingFolder = folderRepository.getOne(id);

        // update attributes
        existingFolder.setTitle(json.get("title").asText());
        existingFolder.setDescription(json.get("description").asText());
        existingFolder.setUpdateDate(Instant.now());

        folderRepository.save(existingFolder);

        return "edit Folder successfully";
    }

    @Transactional
    public String deleteFolder(Long id){

        // find a specific folder
        Folder existingFolder = folderRepository.getOne(id);

        folderRepository.deleteById(id);

        return "remove Folder successfully";
    }

    @Transactional
    public ObjectNode getFolderByID(Long id){

        // find a specific folder
        Folder existingFolder = folderRepository.getOne(id);

        // create a json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("folder_id",existingFolder.getId());
        json.put("title",existingFolder.getTitle());
        json.put("description",existingFolder.getDescription());
        json.put("createdDate", formatter.format(existingFolder.getCreatedDate()));

        return json;
    }

    @Transactional
    public List<ObjectNode> getFolderList(){

        // load all folders in database
        List<Folder> folderList = folderRepository.findAll();

        // json load all rooms to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        // load all room to json list
        for (Folder folder: folderList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("folder_id",folder.getId());
            json.put("title",folder.getTitle());
            json.put("description",folder.getDescription());
            json.put("createdDate", formatter.format(folder.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public String addStudySetToFolder(ObjectNode json){

        Long folder_id = null,studySet_id = null;

        // parsing id of folder
        try {

            folder_id = Long.parseLong(json.get("folder_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // parsing id of studySet
        try {

            studySet_id = Long.parseLong(json.get("studySet_id").asText());

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }

        // find that folder
        Folder existingFolder = folderRepository.getOne(folder_id);
        // find that studySet
        StudySet existingStudySet = studySetRepository.getOne(studySet_id);

        //create id of folderStudySet
        FolderStudySetId folderStudySetId = new FolderStudySetId();

        folderStudySetId.setFolderId(folder_id);
        folderStudySetId.setStudySetId(studySet_id);


        FolderStudySet folderStudySet = new FolderStudySet();

        // set attributes form folderStudySet
        folderStudySet.setFolderStudySetId(folderStudySetId);
        folderStudySet.setStudySet(existingStudySet);
        folderStudySet.setFolder(existingFolder);
        folderStudySet.setCreatedDate(Instant.now());

        // add relationship folderStudySet
        existingFolder.getFolderStudySets().add(folderStudySet);

        folderRepository.saveAndFlush(existingFolder);

        return "add StudySet to Folder successfully";
    }

    @Transactional
    public String deleteStudySetFromFolder(Long folder_id,Long studySet_id){

        // find that folder
        Folder existingFolder = folderRepository.getOne(folder_id);
        // find that studySet
        StudySet existingStudySet = studySetRepository.getOne(studySet_id);

        // find folderStudySet in folderStudySetList of a folder
        FolderStudySet existingFolderStudySet =existingFolder.getFolderStudySets().stream().filter(
                folderStudySet ->
                        folderStudySet.getFolderStudySetId().getFolderId().equals(folder_id) &&
                                folderStudySet.getFolderStudySetId().getStudySetId().equals(studySet_id)

        ).findAny().orElse(null);

        if(null == existingFolderStudySet)
        {
            // cancel remove because no relationship
            return "Folder dosen't include StudySet";
        }

        // remove relationship roomMember
        existingFolder.getFolderStudySets().remove(existingFolderStudySet);

        folderRepository.saveAndFlush(existingFolder);

        return "remove StudySet from Folder successfully";
    }

    @Transactional
    public List<ObjectNode> getFolderStudySetList(Long id){

        // find specific folder
        Folder existingFolder = folderRepository.getOne(id);


        // load all folderStudySets in database
        List<FolderStudySet> folderStudySetList = existingFolder.getFolderStudySets();

        // json load all roomStudySets to client
        List<ObjectNode> objectNodeList = new ArrayList<>();

        // helper create objectnode
        ObjectMapper mapper;

        // load all folderStudySets to json list
        for (FolderStudySet folderStudySet: folderStudySetList) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("studySet_id",folderStudySet.getFolderStudySetId().getStudySetId());
            json.put("title", folderStudySet.getStudySet().getTitle());
            json.put("description",folderStudySet.getStudySet().getDescription());
            json.put("createdDate", formatter.format(folderStudySet.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    public boolean isCreatorOfFolder(Long folder_id){

        // find specific folder
        Folder existingFolder = folderRepository.getOne(folder_id);

        // get creator of folder
        User creatorOfFolder = existingFolder.getOwner();

        // get user logined
        User currenUserLogined = authService.getCurrentUser();

        // verify user is creator of folder
        if(currenUserLogined.getId().equals(creatorOfFolder.getId()))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public Color[] listColorForFolder(){


        return Color.values();

    }
}
