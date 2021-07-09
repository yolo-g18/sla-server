package com.g18.service;

import java.util.ArrayList;
import java.util.List;

import com.g18.dto.CardDto;
import com.g18.dto.StudySetRequest;
import com.g18.dto.StudySetResponse;

import com.g18.entity.Card;
import com.g18.entity.User;
import com.g18.entity.StudySet;

import com.g18.repository.CardLearningRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.CardRepository;
import com.g18.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class StudySetService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardLearningRepository cardLearningRepository;



    public String createStudySet(StudySetRequest request) {
    	Long userId = authService.getCurrentAccount().getUser().getId();

        try{
            User user = userRepository.findById(userId).orElse(null);

            StudySet studySet = new StudySet();
            studySet.setCreator(user);
            studySet.setDescription(request.getDescription());
            studySet.setTag(request.getTag());
            studySet.setTitle(request.getTitle());
            studySet.setPublic(request.isPublic());
            List<Card> listCard = request.getCards();
            for (Card card: listCard) {
                card.setStudySet(studySet);
            }
            studySet.setCards(listCard);
            return studySetRepository.save(studySet).getId().toString();
        }catch (Exception e){
            log.info(e.getMessage());
            return "add Study Set fail";
        }

    }

    public String deleteStudySet(Long id) {
        StudySet studySet = studySetRepository.findById(id).orElse(null);
        User auth = authService.getCurrentAccount().getUser();
        if(studySet != null && auth.equals(studySet.getCreator())){
            studySetRepository.deleteById(id);
            return "delete StudySet successfully";
        }else{
            return "Not permitted";
        }
    }

    public String editStudySet(StudySetRequest request) {
        Long userId = authService.getCurrentAccount().getUser().getId();

        try{
            User user = userRepository.findById(userId).orElse(null);

            StudySet studySet = studySetRepository.findById(request.getId())
                                        .orElseThrow(()->  new ExpressionException("Study Set not exist"));;
            if(user.equals(studySet.getCreator())){
                studySet.setDescription(request.getDescription());
                studySet.setTag(request.getTag());
                studySet.setTitle(request.getTitle());
                studySet.setPublic(request.isPublic());

                studySetRepository.save(studySet);
                return "update StudySet successfully";
            }else{
                return "Not permitted";
            }

        }catch (Exception e){
            log.info(e.getMessage());
            return "update Study Set fail";
        }
    }

    public ResponseEntity listStudySet() {
        Long id = authService.getCurrentUser().getId();
        try{
            List<StudySet> studySetList = userRepository.findById(id).orElse(null).getStudySetsOwn();
            List<StudySetResponse> responses = new ArrayList<>();
            for (StudySet studySet: studySetList) {
                StudySetResponse studySetResponse = setStudySetResponse(studySet);
                responses.add(studySetResponse);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity viewStudySetBy(Long id) {

        StudySet studySet = studySetRepository.findById(id)
                                    .orElseThrow(()->  new ExpressionException("Study Set not exist"));;

        StudySetResponse studySetResponse = setStudySetResponse(studySet);
        return ResponseEntity.status(HttpStatus.CREATED).body(studySetResponse);
    }

    public String shareStudySetBy(StudySetRequest request) {
        // TODO Auto-generated method stub
        return "share StudySet successfully";
    }

    public String exportStudySet(StudySetRequest request){
        // TODO Auto-generated method stub
        return "export StudySet successfully";
    }

    private StudySetResponse setStudySetResponse(StudySet studySet){
        StudySetResponse studySetResponse = new StudySetResponse();
        studySetResponse.setId(studySet.getId());
        studySetResponse.setUsername(authService.getCurrentAccount().getUsername());
        studySetResponse.setDescription(studySet.getDescription());
        studySetResponse.setTag(studySet.getTag());
        studySetResponse.setTitle(studySet.getTitle());
        studySetResponse.setPublic(studySet.isPublic());
        studySetResponse.setNumberOfCard(studySet.getCards().size());
        return studySetResponse;
    }
}
