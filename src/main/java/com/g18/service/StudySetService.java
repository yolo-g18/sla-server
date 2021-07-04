package com.g18.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.g18.dto.CardDto;
import com.g18.dto.StudySetRequest;
import com.g18.dto.StudySetResponse;
import com.g18.entity.Card;
import com.g18.entity.CardLearning;
import com.g18.entity.User;
import com.g18.entity.StudySet;
import com.g18.repository.CardLearningRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.CardRepository;
import com.g18.repository.UserRepository;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
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

    private Log log;

    public String createStudySet(StudySetRequest request) {
    	Long userId = authService.getCurrentUser().getId();

        if(request.getCreator().equals(userId)){
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
                studySetRepository.save(studySet);
                return "add StudySet successfully";
            }catch (Exception e){
                log.info(e.getMessage());
                return "add Study Set fail";
            }
        }else {
            return "User account conflict";
        }
    }

    public String deleteStudySet(Long id) {
        studySetRepository.deleteById(id);
        return "delete StudySet successfully";
    }

    public String editStudySet(StudySetRequest request) {
        Long userId = authService.getCurrentUser().getId();

        if(request.getCreator().equals(userId)){
            try{
                User user = userRepository.findById(userId).orElse(null);

                StudySet studySet = studySetRepository.findById(request.getId()).orElse(null);

                studySet.setCreator(user);
                studySet.setDescription(request.getDescription());
                studySet.setTag(request.getTag());
                studySet.setTitle(request.getTitle());
                studySet.setPublic(request.isPublic());

                studySetRepository.save(studySet);
                return "update StudySet successfully";
            }catch (Exception e){
                log.info(e.getMessage());
                return "update Study Set fail";
            }
        }else {
            return "User account conflict";
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

        StudySet studySet = studySetRepository.findById(id).orElse(null);
        if(studySet != null){
            StudySetResponse studySetResponse = setStudySetResponse(studySet);
            return ResponseEntity.status(HttpStatus.CREATED).body(studySetResponse);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("Study Set not exist");
        }
    }

    public String shareStudySetBy(StudySetRequest request) {
        // TODO Auto-generated method stub
        return "share StudySet successfully";
    }

    public String exportStudySet(StudySetRequest request){
        // TODO Auto-generated method stub
        return "export StudySet successfully";
    }


    public String learningFlashCard(Long id){
        // TODO Auto-generated method stub
        return "learning FlashCard";

    }

    private List<CardDto> convertCardToCardDto(List<Card> cards){
        List<CardDto> cardDtoList = new ArrayList<>();
        for (Card card : cards) {
            CardDto cardDto = new CardDto();
            cardDto.setStudySet(card.getStudySet().getId());
            cardDto.setBack(card.getBack());
            cardDto.setFront(card.getFront());
            cardDtoList.add(cardDto);
        }
        return cardDtoList;
    }

    private StudySetResponse setStudySetResponse(StudySet studySet){
        StudySetResponse studySetResponse = new StudySetResponse();
        studySetResponse.setId(studySet.getId());
        studySetResponse.setCreator(studySet.getCreator().getId());
        studySetResponse.setDescription(studySet.getDescription());
        studySetResponse.setTag(studySet.getTag());
        studySetResponse.setTitle(studySet.getTitle());
        studySetResponse.setPublic(studySet.isPublic());

        List<CardDto> cardDtoList = new ArrayList<>();
        for (Card card : studySet.getCards()) {
            CardDto cardDto = new CardDto();
            cardDto.setStudySet(card.getStudySet().getId());
            cardDto.setBack(card.getBack());
            cardDto.setFront(card.getFront());
            cardDtoList.add(cardDto);
        }
        studySetResponse.setCards(cardDtoList);
        return studySetResponse;
    }


}
