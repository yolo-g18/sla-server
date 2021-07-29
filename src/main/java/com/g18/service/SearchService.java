package com.g18.service;

import com.g18.dto.*;
import com.g18.entity.*;
import com.g18.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FolderStudySetRepository folderStudySetRepository;

    @Autowired
    private RoomService roomService;


    public Page<SearchStudySetResponse> searchStudySetByTitle(Pageable pageable, String keySearch) {
        Page<StudySet> studySetPage =studySetRepository.findByTitleContainsAndIsPublicTrue(keySearch,pageable);
        int totalElements = (int) studySetPage.getTotalElements();
        return new PageImpl<SearchStudySetResponse>(
                studySetPage.stream().map(studySet -> new SearchStudySetResponse(
                          studySet.getId(),
                        findUserNameByUserId(studySet.getCreator().getId()),
                        studySet.getTitle(),
                        studySet.getDescription(),
                        convertToCardDto(cardRepository.findTop4ByStudySetId(studySet.getId())),
                        studySet.getTag(),
                        studySet.getCards().size(),
                        studySet.getCreatedDate()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    //Search list public Study set by tag
    public Page<SearchStudySetResponse> searchStudySetByTag(Pageable pageable, String keySearch) {
        Page<StudySet> studySetPage =studySetRepository.findByTagContainsAndIsPublicTrue(keySearch,pageable);
        int totalElements = (int) studySetPage.getTotalElements();
        return new PageImpl<SearchStudySetResponse>(
                studySetPage.stream().map(studySet -> new SearchStudySetResponse(
                                studySet.getId(),
                        findUserNameByUserId(studySet.getCreator().getId()),
                                studySet.getTitle(),
                                studySet.getDescription(),
                                convertToCardDto(cardRepository.findTop4ByStudySetId(studySet.getId())),
                                studySet.getTag(),
                                studySet.getCards().size(),
                                studySet.getCreatedDate()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }


    public Page<EventDto> searchEventByTitle(Pageable pageable, String nameSearch) {
        Page<Event> eventPage = eventRepository.findByNameContaining(nameSearch,pageable);
        int totalElements = (int) eventPage.getTotalElements();
        return new PageImpl<EventDto>(
                eventPage.stream().map(event -> new EventDto(
                        event.getId(),event.getUser().getId(),event.getName(),event.getDescription(),event.isLearnEvent(),
                        String.valueOf(event.getFromTime()), String.valueOf(event.getToTime()),
                        event.getColor(),String.valueOf(event.getCreatedTime()),String.valueOf(event.getUpdateTime())
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    public Page<SearchFolderResponse> searchFolderByTitle(Pageable pageable, String titleSearch) {
        Page<Folder> folderPage = folderRepository.findByTitleContaining(titleSearch,pageable);
        int totalElements = (int) folderPage.getTotalElements();
        return new PageImpl<SearchFolderResponse>(
                folderPage.stream().map(
                        folder -> new SearchFolderResponse(
                                folder.getId(),
                                findUserNameByUserId(folder.getOwner().getId()),
                                folder.getTitle(),
                                folder.getDescription(),
                                folder.getCreatedDate(),
                                folder.getCreatedDate(),
                                folder.getColor(),
                                folder.getFolderStudySets().size()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }
    //Search user by username and paging
    public Page<SearchUserResponse> searchUserByUsername(Pageable pageable, String username) {
        Page<Account> accountPage = accountRepository.findByUsernameContains(username,pageable);
        int totalElements = (int) accountPage.getTotalElements();
        return new PageImpl<SearchUserResponse>(
                accountPage.stream().map(
                        account -> new SearchUserResponse(
                                account.getUsername(),
                                account.getUser().getAvatar(),
                                account.getUser().getAvatar(),
                                account.getUser().getStudySetsOwn().size()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    // Search room by name and paging
    public Page<SearchRoomResponse> searchRoomByName(Pageable pageable, String nameSearch) {
        Page<Room> roomPage = roomRepository.findByNameContaining(nameSearch,pageable);
        int totalElements = (int) roomPage.getTotalElements();
        return new PageImpl<SearchRoomResponse>(
                roomPage.stream().map(
                        room -> new SearchRoomResponse(
                            room.getId(),
                            findUserNameByUserId(room.getOwner().getId()),
                                room.getName(),
                                room.getDescription(),
                                room.getCreatedDate(),
                                room.getUpdateDate(),
                                //Error when find number of member (memberId and roomID)
                                room.getRoomMembers().size(),
                                //Error when find number of studyset (memberId and roomID)
                                roomService.listIdOfSetsInRoom(room.getId()).size()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    //search list user by username
    public List<SearchUserResponse> getAllUserByUsername(String username){
        List<Account> accounts = accountRepository.findByUsernameContaining(username);
        List<SearchUserResponse> listResponse = new ArrayList<>();
        for (Account acc : accounts){
            SearchUserResponse accResponse = new SearchUserResponse();
            accResponse.setUsername(acc.getUsername());
            accResponse.setAvatar(acc.getUser().getAvatar());
            accResponse.setBio(acc.getUser().getBio());
            accResponse.setNumberStudySetOwn(acc.getUser().getStudySetsOwn().size());
            listResponse.add(accResponse);
        }
        return listResponse;
    }


    public String findUserNameByUserId(long uid){
        return accountRepository.findUserNameByUserId(uid);
    }

    private List<CardSearchResponse> convertToCardDto(List<Card> cards){
        List<CardSearchResponse> cardsDto = new ArrayList<>();
        for (Card c : cards){
            CardSearchResponse cDto = new CardSearchResponse();
            cDto.setId(c.getId());
            cDto.setFront(c.getFront());
            cDto.setBack(c.getBack());
            cDto.setStudySetID(c.getStudySet().getId());
            cardsDto.add(cDto);
        }
        return cardsDto;
    }

}
