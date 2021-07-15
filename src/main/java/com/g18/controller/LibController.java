package com.g18.controller;

import com.g18.dto.SearchFolderResponse;
import com.g18.dto.SearchRoomResponse;
import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningResponse;
import com.g18.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lib")
public class LibController {
    @Autowired
    private LibraryService libraryService;

    @GetMapping("ss/learning")
    List<StudySetLearningResponse> getStudySetsLearning(){
        return libraryService.getLearningStudySets();
    }
    @GetMapping("ss/created")
    List<SearchStudySetResponse> getCreatedStudySet(@RequestParam long userId){
        return libraryService.getStudySetCreatedByUserId(userId);
    }

    @GetMapping("/folder/{creatorId}/{title}")
    List<SearchFolderResponse> getFolderByUserIdAndTitle(@PathVariable long creatorId, @PathVariable String title){
        return libraryService.getFolderByCreatorIdAndTitle(creatorId,title);
    }

    @GetMapping("/room/{ownerId}/{title}")
    List<SearchRoomResponse> getRoomByUserIdAndTitle(@PathVariable long ownerId, @PathVariable String title){
        return libraryService.getRoomByOwnerIDIdAndTitle(ownerId,title);
    }

}
