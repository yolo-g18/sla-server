package com.g18.controller;

import com.g18.dto.FolderRequest;
import com.g18.dto.FolderResponse;
import com.g18.entity.Folder;
import com.g18.service.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/folder")
@AllArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody FolderRequest folderRequest) {
        folderService.createFolder(folderRequest);
        return new ResponseEntity<>("Create Folder Successful", HttpStatus.CREATED);
    }

//    //get folder to view
//    @GetMapping("/sets/{folder_id}")
//    public ResponseEntity<Folder> getFolderById(@PathVariable("id") Long id) {
//        Folder folder = folderService.getFolderById(id);
//        return new ResponseEntity<>(folder, HttpStatus.OK);
//    }

    //get all folder by owner id
    @GetMapping("/{owner_id}")
    public List<FolderResponse> getAllFolderByOwnerId(@PathVariable Long owner_id) {
        return folderService.getAllFolder(owner_id);
    }

    @PutMapping("/edit")
    public ResponseEntity<String> edit(@RequestBody FolderRequest folderRequest) {
        folderService.editFolder(folderRequest);
        return new ResponseEntity<>("Update Folder Successful", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return new ResponseEntity<>("Delete Folder Successful", HttpStatus.OK);
    }

   /* @PostMapping("/share")
    public ResponseEntity<String> share(@Valid @RequestBody FolderRequest folderRequest) {
        folderService.createFolder(folderRequest);
        return new ResponseEntity<>("Create Folder Successful", HttpStatus.OK);
    }
*/

}
