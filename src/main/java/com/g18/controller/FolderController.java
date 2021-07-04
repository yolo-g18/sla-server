package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}