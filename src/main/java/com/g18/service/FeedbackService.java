package com.g18.service;

import com.g18.dto.FeedbackDto;
import com.g18.dto.ReportDto;
import com.g18.entity.Report;
import com.g18.entity.StudySetLearning;
import com.g18.repository.ReportRepository;
import com.g18.repository.StudySetLearningRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private StudySetLearningRepository studySetLearningRepository;
    @Autowired
    private AuthService authService;

    @Transactional
    public String ratingAndFeedback(Long ssId, ObjectNode json){
        StudySetLearning ssl = studySetLearningRepository.findByUserIdAndStudySetId(authService.getCurrentUser().getId(),ssId);
        ssl.setFeedback(json.get("feedback").asText());
        ssl.setRating(json.get("rating").asDouble());
        studySetLearningRepository.save(ssl);
        return "Report successfully";
    }

    public List<FeedbackDto> getAllFeedbackOfStudySet(Long ssId){
        List<StudySetLearning> sslList = studySetLearningRepository.findByStudySetId(ssId);
        List<FeedbackDto> feedbackList = new ArrayList<>();
        for(StudySetLearning ssl : sslList){
            FeedbackDto feedbackDto = new FeedbackDto();
            feedbackDto.setUserId(ssl.getUser().getId());
            feedbackDto.setRating(ssl.getRating());
            feedbackDto.setFeedback(ssl.getFeedback());
            feedbackList.add(feedbackDto);
        }
        return feedbackList;
    }

}
