package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.model.Color;
import com.g18.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("/createFolder")
    public String createFolder(@RequestBody ObjectNode json){ return folderService.saveFolder(json);}

    @PutMapping("/editFolder")
    public String editFolder(@RequestBody ObjectNode json){
        return folderService.editFolder(json);
    }

    @DeleteMapping("/deleteFolder/{id}")
    public String deleteFolder(@PathVariable Long id) {
        return folderService.deleteFolder(id);
    }

    @GetMapping("/getFolder/{id}")
    public ObjectNode getFolder(@PathVariable Long id){
        return folderService.getFolderByID(id);
    }

    @GetMapping("/listFolders")
    public List<ObjectNode> listAllFolders(){
        return folderService.getFolderList();
    }

    @PutMapping("/addStudySetToFolder")
    public String addStudySetToFolder(@RequestBody ObjectNode json){
        return folderService.addStudySetToFolder(json);}

    @DeleteMapping("/deleteStudySetFromFolder/{folder_id}/{studySet_id}")
    public String deleteStudySetFromFolder(@PathVariable Long folder_id, @PathVariable Long studySet_id){
        return folderService.deleteStudySetFromFolder(folder_id,studySet_id);
    }

    @GetMapping("/listStudySetsOfFolder/{id}")
    public List<ObjectNode> listStudySetsOfFolder(@PathVariable Long id){
        return folderService.getFolderStudySetList(id);
    }

    @GetMapping("/isCreatorOfFolder/{folder_id}")
    public boolean isCreatorOfFolder(@PathVariable Long folder_id){
        return folderService.isCreatorOfFolder(folder_id);
    }

    @GetMapping("/getColorFolder")
    public Color[] getColorFolder(){
        return folderService.listColorForFolder();
    }
}
