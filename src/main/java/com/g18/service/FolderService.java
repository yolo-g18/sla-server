package com.g18.service;

import com.g18.dto.FolderRequest;
import com.g18.entity.Folder;

import com.g18.entity.User;
import com.g18.repository.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@AllArgsConstructor

public class FolderService {

    private final FolderRepository folderRepository;
    private final AuthService authService;

    public void createFolder(FolderRequest folderRequest) {
        Folder folder = new Folder();
        User owner = authService.getCurrentUser();
        folder.setOwner(owner);
        folder.setTitle(folderRequest.getTitle());
        folder.setCreatedDate(Instant.now());
        folder.setUpdateDate(Instant.now());
        folder.setDescription(folderRequest.getDescription());
        folder.setColor(folderRequest.getColor());
        folderRepository.save(folder);
    }
}
