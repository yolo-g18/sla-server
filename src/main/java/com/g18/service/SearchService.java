package com.g18.service;

import com.g18.dto.*;
import com.g18.entity.*;
import com.g18.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
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


    public Page<SearchStudySetResponse> searchStudySetByTitle(Pageable pageable, String keySearch) {
        Page<StudySet> studySetPage =studySetRepository.findByTitleContainsAndIsPublicTrue(keySearch,pageable);
        int totalElements = (int) studySetPage.getTotalElements();
        return new PageImpl<SearchStudySetResponse>(
                studySetPage.stream().map(studySet -> new SearchStudySetResponse(
                          studySet.getId(),
                        studySet.getCreator().getLastName() + " " + studySet.getCreator().getFirstName(),
                        studySet.getTitle(),
                        convertToCardDto(cardRepository.findTop4ByStudySetId(studySet.getId())),
                        studySet.getCards().size()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }






    public Page<EventDto> searchEvent(Pageable pageable,String nameSearch) {
        Page<Event> eventPage =eventRepository.findByNameContaining(nameSearch,pageable);
        int totalElements = (int) eventPage.getTotalElements();
        return new PageImpl<EventDto>(
                eventPage.stream().map(event -> new EventDto(
                        event.getId(),event.getUser().getId(),event.getName(),event.getDescription(),event.getFromTime(),
                        event.getToTime(),event.getColor(),event.getCreatedTime(),event.getUpdateTime()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    public Page<SearchFolderResponse> searchFolder(Pageable pageable, String titleSearch) {
        Page<Folder> folderPage = folderRepository.findByTitleContaining(titleSearch,pageable);
        int totalElements = folderPage.getNumberOfElements();
        return new PageImpl<SearchFolderResponse>(
                folderPage.stream().map(
                        folder -> new SearchFolderResponse(
                                folder.getId(),
                                folder.getOwner().getLastName() + "" + folder.getOwner().getFirstName(),
                                folder.getTitle(),
                                folder.getDescription(),
                                folder.getCreatedDate(),
                                folder.getCreatedDate(),
                                folder.getColor(),
                                folder.getFolderStudySets().size()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }


    public Page<SearchRoomResponse> searchRoom(Pageable pageable, String nameSearch) {
        Page<Room> roomPage = roomRepository.findByNameContaining(nameSearch,pageable);
        int totalElements = roomPage.getNumberOfElements();
        return new PageImpl<SearchRoomResponse>(
                roomPage.stream().map(
                        room -> new SearchRoomResponse(
                            room.getId(),
                            room.getOwner().getLastName() + " " +room.getOwner().getFirstName(),
                                room.getName(),
                                room.getDescription(),
                                room.getCreatedDate(),
                                room.getUpdateDate(),
                                //Error when find number of member (memberId and roomID)
                                room.getRoomMembers().size(),
                                //Error when find number of studyset (memberId and roomID)
                                getTotalStudySetsInRoom(room)
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }

    private int getTotalStudySetsInRoom(Room room){
        int total = 0;
        total += room.getRoomStudySets().size();
        for (RoomFolder rf : room.getRoomFolders()){
            total += rf.getFolder().getFolderStudySets().size();
        }
        return total;

    }

    private List<CardDto> convertToCardDto(List<Card> cards){
        List<CardDto> cardsDto = new ArrayList<>();
        for (Card c : cards){
            CardDto cDto = new CardDto();
            cDto.setId(c.getId());
            cDto.setFront(c.getFront());
            cDto.setBack(c.getBack());
            cDto.setStudySetID(c.getStudySet().getId());
            cardsDto.add(cDto);
        }
        return cardsDto;
    }

}
