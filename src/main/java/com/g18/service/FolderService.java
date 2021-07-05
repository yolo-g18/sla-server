package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.*;
import com.g18.exceptions.*;
import com.g18.model.Color;
import com.g18.model.FolderStudySetId;

import com.g18.repository.FolderRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
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
        folder.setColor(Color.valueOf(json.get("color").asText()));
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

        // verify folder's permisson
        if(isCreatorOfFolder(id) == false)
            return "You are not creator of Folder, You don't have permisson!!!";

        // find that specific room
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException());

        // update attributes
        existingFolder.setTitle(json.get("title").asText());
        existingFolder.setDescription(json.get("description").asText());
        existingFolder.setColor(Color.valueOf(json.get("color").asText()));
        existingFolder.setUpdateDate(Instant.now());

        folderRepository.save(existingFolder);

        return "edit Folder successfully";
    }



    @Transactional
    public ObjectNode getFolderByID(Long id){

        // find a specific folder
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException());

        // create a json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("folder_id",existingFolder.getId());
        json.put("title",existingFolder.getTitle());
        json.put("color",existingFolder.getColor().toString());
        json.put("description",existingFolder.getDescription());
        json.put("createdDate", formatter.format(existingFolder.getCreatedDate()));

        return json;
    }

    @Transactional
    public List<ObjectNode> getFolderList(){

        // load all folders in database
        List<Folder> folderList = folderRepository.findAll();

        if(folderList.isEmpty())
        {
            throw new NoDataFoundException();
        }

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
            json.put("color",folder.getColor().toString());
            json.put("description",folder.getDescription());
            json.put("createdDate", formatter.format(folder.getCreatedDate()));
            objectNodeList.add(json);
        }

        return objectNodeList;
    }

    @Transactional
    public String deleteFolder(Long id){

        // verify folder's permisson
        if(isCreatorOfFolder(id) == false)
            return "You are not creator of Folder, You don't have permisson!!!";

        folderRepository.deleteById(id);

        return "remove Folder successfully";
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
        Folder existingFolder = folderRepository.findById(folder_id).orElseThrow(() -> new FolderNotFoundException());
        // find that studySet
        StudySet existingStudySet = studySetRepository.findById(studySet_id).orElseThrow(() -> new StudySetNotFoundException());

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
        Folder existingFolder = folderRepository.findById(folder_id).orElseThrow(() -> new FolderNotFoundException());
        // find that studySet
        StudySet existingStudySet = studySetRepository.findById(studySet_id).orElseThrow(() -> new StudySetNotFoundException());

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

        // find that folder
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException());

        // load all folderStudySets in database
        List<FolderStudySet> folderStudySetList = existingFolder.getFolderStudySets();

        if(folderStudySetList.isEmpty())
        {
            throw new NoDataFoundException();
        }

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
        Folder existingFolder = folderRepository.findById(folder_id).orElseThrow(() -> new FolderNotFoundException());

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
