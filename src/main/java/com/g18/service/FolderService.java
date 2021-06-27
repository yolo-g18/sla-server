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
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor

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
            @folderID: not null
            }
    @Description: View folder by folderID
    */
    public List<FolderResponse> getAllFolder(Long id) {
        if(id == (Long) id){
            throw new SLAException("Invalid ID");
        }
        User user =  (User) userRepository.findById(id).orElseThrow(() -> new SLAException("User not found"));
        Account account = (Account) accountRepository.findByUser(user).orElseThrow(() -> new SLAException("Not found"));
        List<Folder> folders = folderRepository.getFolderByOwner(user);
        List<FolderResponse> folderResponses = new ArrayList<>();

        for (Folder folder: folders) {
            folderResponses.add(new FolderResponse(account.getUsername(),
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

    }

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @folderID : not null
            }
    @Description: Edit folder by folderID
    */
    public void deleteFolder(FolderRequest folderRequest) {

    }

    /*
    @Author : DuongBHT
    @Date : 27/06/2021
    @param:{
            @folderID : not null
            @creatorID :
            }
    @Description:
    */
    public void shareFolder(FolderRequest folderRequest) {

    }

}
