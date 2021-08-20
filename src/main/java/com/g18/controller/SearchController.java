package com.g18.controller;

import com.g18.dto.*;
import com.g18.entity.Event;
import com.g18.repository.EventRepository;
import com.g18.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/search")
public class SearchController {
    @Autowired
    SearchService searchService;
    @Autowired
    EventRepository eventRepository;

    @GetMapping("/studySet/tag")
    Page<SearchStudySetResponse> searchStudySetByTag(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchStudySetByTag(pageable,keySearch);
    }

    @GetMapping("/studySet/title")
    Page<SearchStudySetResponse> searchStudySetByTitle(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchStudySetByTitle(pageable,keySearch);
    }

    @GetMapping("/event")
    Page<EventDto> searchEvent(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchEventByTitle(pageable,keySearch);
    }

    @GetMapping("/folder")
    Page<SearchFolderResponse> searchFolder(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchFolderByTitle(pageable ,keySearch);
    }

    @GetMapping("/room")
    Page<SearchRoomResponse> searchRoom(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchRoomByName(pageable ,keySearch);
    }
    @GetMapping("/user")
    Page<SearchUserResponse> searchUserAndPaging(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchUserByUsername(pageable ,keySearch);
    }
    @GetMapping(value = "/user/{username}")
    List<SearchUserResponse> searchUserByUsername(@PathVariable("username")String username){
        return searchService.getAllUserByUsername(username);
    }

    @GetMapping(value = "/user/{username}/{roomId}")
    List<SearchUserResponse> getAllInvitingGuessByUsername(@PathVariable("username")String username,
                                                           @PathVariable("roomId")Long roomId){
        return searchService.getAllInvitingGuessByUsername(username,roomId);
    }

}

