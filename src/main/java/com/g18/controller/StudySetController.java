package com.g18.controller;

import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.SearchUserResponse;
import com.g18.service.StudySetService;
import com.g18.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("StudySet")
public class StudySetController {
//    Search studyset
    @Autowired
    private StudySetService studySetService;

    @Autowired
    private UserService userService;

//    @GetMapping(value = "/search")
//    public List<SearchStudySetResponse> searchStudySet(@RequestParam String keyword,
//                                                       @RequestParam int page,
//                                                       @RequestParam int limit){
//        Pageable pageable = (Pageable) PageRequest.of(page,limit);
//
//        return  studySetService.searchStudySet(keyword,pageable);
//    }

    @GetMapping(value = "/searchUser")
    public List<SearchUserResponse> searchUser(@RequestParam String keyword){
        return  userService.searchUser(keyword);
    }
}
