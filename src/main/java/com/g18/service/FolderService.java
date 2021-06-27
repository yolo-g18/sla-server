package com.g18.service;

import com.g18.dto.FolderRequest;
import com.g18.dto.FolderResponse;
import com.g18.entity.Account;
import com.g18.entity.Folder;

import com.g18.entity.StudySet;
import com.g18.entity.User;
import com.g18.exceptions.SLAException;
import com.g18.repository.AccountRepository;
import com.g18.repository.FolderRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.FolderNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final StudySetRepository studySetRepository;

    /*
    @Author: DuongBHT
    @Date: 27/06/2021
    @param:{
            @Title : not null
            @Description : not null
            @Color : not null
            }
    @Description: Create new Folder
    */
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

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @folderID: not null
            }
    @Description: View all folder
    */
   /* public Optional<Folder> getAllFolder() {
        Optional<Folder> folders = Optional.ofNullable(folderRepository.getAllFolder().orElseThrow(() -> new UsernameNotFoundException("No Folder")));
        return folders;
    }*/

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @owner ID: not null
            }
    @Description: View folder by folderID
    */
    public List<FolderResponse> getAllFolder(Long id) {

        // parsing id of room need edit
        /*try {
            id = Long.parseLong(id.toString());
        }catch (Exception e){
            throw new SLAException("Invalid id");
        }*/
        User user = (User) userRepository.findById(id).orElseThrow(() -> new SLAException("User not found"));
        Account account = (Account) accountRepository.findByUser(user).orElseThrow(() -> new SLAException("Not found"));
        List<Folder> folders = folderRepository.getFolderByOwner(user);
        List<FolderResponse> folderResponses = new ArrayList<>();

        for (Folder folder : folders) {
            folderResponses.add(new FolderResponse(account.getUsername(),
                    folder.getId(),
                    folder.getTitle(),
                    folder.getDescription(),
                    folder.getCreatedDate(),
                    folder.getUpdateDate(),
                    folder.getColor(),
                    folder.getFolderStudySets().size()));
        }
        return folderResponses;
    }

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @folderID: not null
            @update-date: sysdate()
            @Title : (not) null
            @Description : (not) null
            @Color : (not) null
            }
    @Description: Edit folder by folderID
    */
    public void editFolder(FolderRequest folderRequest) {
        if (!checkFolderExistence(folderRequest.getId())) {
            throw new SLAException("Folder not found");
        } else {
            Folder existingFolder = folderRepository.findById(folderRequest.getId()).orElse(null);

            if (!existingFolder.getTitle().equalsIgnoreCase(folderRequest.getTitle())) {
                existingFolder.setTitle(folderRequest.getTitle());
                existingFolder.setUpdateDate(Instant.now());
            }
  /*          else {
                existingFolder.setTitle();
            }*/
            if (!existingFolder.getDescription().equalsIgnoreCase(folderRequest.getTitle())) {
                existingFolder.setDescription(folderRequest.getDescription());
                existingFolder.setUpdateDate(Instant.now());
            }
            if (!existingFolder.getColor().equals(folderRequest.getColor())) {
                existingFolder.setColor(folderRequest.getColor());
                existingFolder.setUpdateDate(Instant.now());
            }

            existingFolder.setUpdateDate(Instant.now());

            try {
                folderRepository.save(existingFolder);
            } catch (SLAException ex) {
                log.error(String.valueOf(ex));
            }
        }

    }

    private boolean checkFolderExistence(Long folderId) {
        Optional<Folder> folder = folderRepository.findById(folderId);
        return folder.isPresent();
    }

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @folderID : not null
            }
    @Description: Edit folder by folderID
    */
    public void deleteFolder(Long folderId) {
        if (!checkFolderExistence(folderId)) {
            throw new SLAException("Folder not found");
        } else {
            Folder existingFolder = folderRepository.findById(folderId).orElse(null);

            try {
                folderRepository.delete(existingFolder);
            } catch (SLAException ex) {
                log.error(String.valueOf(ex));
            }
        }
    }



}
