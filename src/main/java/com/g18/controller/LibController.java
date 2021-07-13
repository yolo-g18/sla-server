package com.g18.controller;

import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningDto;
import com.g18.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lib")
public class LibController {
    @Autowired
    private LibraryService libraryService;

    @GetMapping("ss/learning")
    List<StudySetLearningDto> getStudySetsLearning(){
        return libraryService.getLearningStudySets();
    }
    @GetMapping("ss/created")
    List<SearchStudySetResponse> getCreatedStudySet(){
        return libraryService.getStudySetCreatedByCurrentUser();
    }

}
