package com.g18.controller;


import com.g18.dto.FeedbackRequest;

import com.g18.service.FeedbackService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PutMapping(value = "/api/feedback/edit")
    public ResponseEntity editFeedback(@Valid @RequestBody FeedbackRequest request){
        return feedbackService.editFeedback(request);
    }

    @GetMapping(value = "/api/feedback/{studySetId}/me")
    public ResponseEntity getRatingOfCurrentUser(@PathVariable("studySetId") long studySetId){
        return feedbackService.getRatingOfCurrentUser(studySetId);
    }

    @GetMapping(value = "/api/feedback/{studySetId}")
    public ResponseEntity getListRatingOfStudySet(@PathVariable("studySetId") long studySetId){
        return feedbackService.getListRatingOfStudySet(studySetId);
    }
}
