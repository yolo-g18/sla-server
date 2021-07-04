package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.Folder;
import com.g18.entity.Room;
import com.g18.entity.User;
import com.g18.exceptions.RoomNotFoundException;
import com.g18.repository.FolderRepository;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class FolderService {

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
            .withLocale( Locale.UK )
            .withZone( ZoneId.systemDefault() );

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

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
}
