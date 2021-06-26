package com.g18.controller;

import com.g18.dto.FolderRequest;
import com.g18.service.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import javax.validation.Valid;

@RestController
@RequestMapping("api/folder")
@AllArgsConstructor

public class FolderController {

    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody FolderRequest folderRequest) {
        folderService.createFolder(folderRequest);
        return new ResponseEntity<>("Create Folder Successful", HttpStatus.OK);
    }

}
