package com.g18.service;

import com.g18.dto.FeedbackRequest;
import com.g18.dto.FeedbackResponse;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import com.g18.repository.AccountRepository;
import com.g18.repository.StudySetLearningRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FeedbackService {

    @Autowired
    private StudySetLearningRepository studySetLearningRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity editFeedback(FeedbackRequest request) {
        try{
            User user = authService.getCurrentAccount().getUser();
            StudySetLearning studySetLearning = studySetLearningRepository.findByUserIdAndStudySetId(user.getId(), request.getStudySetId());
            if(studySetLearning != null){
                if(!studySetLearning.getStudySet().getCreator().equals(user)){
                    studySetLearning.setRating(request.getRating());
                    studySetLearning.setFeedback(request.getFeedback());
                    studySetLearningRepository.save(studySetLearning);

                    return ResponseEntity.status(HttpStatus.OK).body("update feedback successfully");
                }else{
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Creator don't rate yourself.");
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has not started learning");
            }
        }catch (Exception e){
            log.info("editFeedbackException: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity getRatingOfCurrentUser(Long studySetId) {
        try{
            Long userId = authService.getCurrentAccount().getUser().getId();
            StudySetLearning studySetLearning = studySetLearningRepository.findByUserIdAndStudySetId(userId, studySetId);
            if(studySetLearning != null){
                FeedbackResponse response = new FeedbackResponse();
                String userName = authService.getCurrentAccount().getUsername();
                response.setUserName(userName);
                response.setRating(studySetLearning.getRating());
                response.setFeedback(studySetLearning.getFeedback());

                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has not started learning");
            }
        }catch (Exception e){
            log.info("getRatingOfCurrentUser: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity getListRatingOfStudySet(long studySetId) {
        try{
            List<FeedbackResponse> response = new ArrayList<>();

            List<StudySetLearning> studySetLearningList = studySetLearningRepository.findByStudySetId(studySetId);
            if(!studySetLearningList.isEmpty()){
                for(StudySetLearning studySetLearning : studySetLearningList){
                    FeedbackResponse feedbackResponse = new FeedbackResponse();

                    String userName = accountRepository.findUserNameByUserId(studySetLearning.getUser().getId());
                    feedbackResponse.setUserName(userName);
                    feedbackResponse.setRating(studySetLearning.getRating());
                    feedbackResponse.setFeedback(studySetLearning.getFeedback());

                    response.add(feedbackResponse);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            log.info("getListRatingOfStudySet: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
