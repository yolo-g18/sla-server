package com.g18.service;

import com.g18.dto.SearchFolderResponse;
import com.g18.dto.SearchRoomResponse;
import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningResponse;
import com.g18.entity.*;
import com.g18.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private AuthService authService;
    @Autowired
    private StudySetLearningRepository studySetLearningRepository;
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private FolderStudySetRepository folderStudySetRepository;
    @Autowired
    private CardRepository cardRepository;


    //get all study sets current user have created
    public List<StudySetLearningResponse> getLearningStudySets(){
        List<StudySetLearningResponse> results = new ArrayList<>();
        User currentUser = authService.getCurrentUser();
        List<StudySetLearning> listLearningSS = studySetLearningRepository.findStudySetLearningByUserId(currentUser.getId());
        for(StudySetLearning ssl : listLearningSS){
            StudySetLearningResponse sslDto = new StudySetLearningResponse();
            sslDto.setStudySetId(ssl.getStudySet().getId());
            sslDto.setUserID(ssl.getUser().getId());
            sslDto.setStudySetName(ssl.getStudySet().getTitle());
            sslDto.setSsDescription(ssl.getStudySet().getDescription());
            sslDto.setColor(ssl.getColor());
            sslDto.setProgress(ssl.getProgress());
            sslDto.setRating(ssl.getRating());
            sslDto.setStatus(ssl.getStatus());
            sslDto.setNumberOfCards(cardRepository.countNumberCardBySSID(ssl.getStudySet().getId()));
            results.add(sslDto);
        }
        return results;
    }
    // get all study sets the current user have created
    public List<SearchStudySetResponse> getStudySetCreatedByUserId(long userId) {
        User currentUser = authService.getCurrentUser();
        List<StudySet> studySetList =studySetRepository.findByCreatorId(userId);
        List<SearchStudySetResponse> result = new ArrayList<>();
        for(StudySet ss : studySetList){
            SearchStudySetResponse sssr = new SearchStudySetResponse();
            sssr.setId(ss.getId());
            sssr.setCreator(findUserNameByUserId(currentUser.getId()));
            sssr.setNumberOfCards(ss.getCards().size());
            sssr.setTitle(ss.getTitle());
            sssr.setTag(ss.getTag());
            result.add(sssr);
        }
        return result;
    }
    //Get list folder created by a user and filter by title
    public List<SearchFolderResponse> getFolderByCreatorIdAndTitle(long creatorId,String title){
        List<SearchFolderResponse> result = new ArrayList<>();
        List<Folder> folderList =  folderRepository.findByCreatorIdAndTitleContaining(creatorId,title);
        for(Folder f : folderList){
            SearchFolderResponse sfr = new SearchFolderResponse();
            sfr.setId(f.getId());
            sfr.setOwnerName(findUserNameByUserId(f.getOwner().getId()));
            sfr.setTitle(f.getTitle());
            sfr.setDescription(f.getDescription());
            sfr.setCreatedDate(f.getCreatedDate());
            sfr.setUpdateDate(f.getUpdateDate());
            sfr.setColor(f.getColor());
            sfr.setNumberOfStudySets(f.getFolderStudySets().size());
            result.add(sfr);
        }
        return result;
    }
    //Get list room created by a user and filter by title
    public List<SearchRoomResponse> getRoomByOwnerIDIdAndTitle(long ownerId, String name){
        List<SearchRoomResponse> result = new ArrayList<>();
        List<Room> roomList =  roomRepository.findByTitleAndOwnerId(ownerId,name);
        for(Room r : roomList){
            SearchRoomResponse srr = new SearchRoomResponse();
            srr.setId(r.getId());
            srr.setOwner(findUserNameByUserId(r.getOwner().getId()));
            srr.setName(r.getName());
            srr.setDescription(r.getDescription());
            srr.setCreatedDate(r.getCreatedDate());
            srr.setUpdateDate(r.getUpdateDate());
            srr.setNumberOfMembers(r.getRoomMembers().size());
            srr.setNumberOfStudySets(getTotalStudySetsInRoom(r));
            result.add(srr);
        }
        return result;
    }


    public String findUserNameByUserId(long uid){
        return accountRepository.findUserNameByUserId(uid);
    }

    private int getTotalStudySetsInRoom(Room room){
        int total = 0;
        total += room.getRoomStudySets().size();
        for (RoomFolder rf : room.getRoomFolders()){
            List<Long> listSSID = folderStudySetRepository.findNumberSSID(rf.getFolder().getId());
            total += listSSID.size();
        }
        return total;
    }


}
