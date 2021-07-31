package com.g18.controller;

import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningResponse;
import com.g18.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/home")
public class HomeController {
    @Autowired
    private HomeService homeService;
    //get top 4 Learning Study Set
    @GetMapping("/SSLearning")
    List<StudySetLearningResponse> getStudySetsLearning(){
        return homeService.getTop6LearningStudySets();
    }
    //get top 4 newest created Study Set
    @GetMapping("/SSCreated")
    List<SearchStudySetResponse> getCreatedStudySet(){
        return homeService.getTop6StudySetCreatedByUserId();
    }

}
