package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.FeedbackResponse;
import com.g18.repository.StudySetLearningRepository;
import com.g18.service.FeedbackService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private StudySetLearningRepository studySetLearningRepository;

    @PutMapping(value = "/api/feedback/{ssId}")
    public String feedbackAndRating(@PathVariable("ssId") Long ssId,
                                  @RequestBody ObjectNode json) throws NotFoundException {
         return feedbackService.ratingAndFeedback(ssId,json);
    }

    @GetMapping(value = "/api/feedback/{ssId}")
    public FeedbackResponse getReportOfStudySet(@PathVariable("ssId") Long ssId) {
        FeedbackResponse response = new FeedbackResponse();
        response.setAvgRating(studySetLearningRepository.getAVGRatingSS(ssId));
        response.setListFeedback(feedbackService.getAllFeedbackOfStudySet(ssId));
       return response;
    }
}
