package com.g18.service;

import com.g18.dto.CardDto;
import com.g18.dto.CardLearningDto;
import com.g18.entity.*;

import com.g18.model.Color;
import com.g18.model.Status;
import com.g18.model.UserCardId;
import com.g18.repository.CardLearningRepository;
import com.g18.repository.CardRepository;
import com.g18.repository.StudySetLearningRepository;
import com.g18.repository.StudySetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CardService {

    @Autowired
    private AuthService authService;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardLearningRepository cardLearningRepository;

    @Autowired
    private StudySetLearningRepository studySetLearningRepository;

    public ResponseEntity createCard(List<CardDto> request) {
        try{
            StudySet studySet = studySetRepository.findById(request.get(0).getStudySet()).orElseThrow(() -> new ExpressionException("Study Set not exist"));
            User user = authService.getCurrentAccount().getUser();
            StudySetLearning studySetLearning = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet, user);

            for (CardDto cardDto: request) {
                Card card = new Card();
                card.setStudySet(studySet);
                card.setBack(cardDto.getBack());
                card.setFront(cardDto.getFront());
                cardRepository.save(card);

            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Add Card successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add Card fail");
        }
    }

    public ResponseEntity editCard(List<CardDto> request) {
        try{
            for (CardDto cardDto: request) {
                Card card = cardRepository.findById(cardDto.getId()).orElseThrow(() -> new ExpressionException("Card not exist"));
                card.setBack(cardDto.getBack());
                card.setFront(cardDto.getFront());
                cardRepository.save(card);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Edit Card successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Edit Card fail");
        }
    }

    public ResponseEntity deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(()->  new ExpressionException("Card not exist"));
        User auth = authService.getCurrentAccount().getUser();

        StudySet studySet = studySetRepository.findById(card.getStudySet().getId())
                                                .orElseThrow(()->  new ExpressionException("Study Set not exist"));

        //Check permission
        if(auth.equals(studySet.getCreator())) {
            cardRepository.delete(card);
            return ResponseEntity.status(HttpStatus.OK).body("Delete Card successfully");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not permitted");
        }
    }

    public ResponseEntity writeHint(CardDto cardDto) {
        try{
            User user = authService.getCurrentAccount().getUser();
            UserCardId userCardId = new UserCardId();
            userCardId.setUserId(user.getId());
            userCardId.setCardId(cardDto.getId());

            CardLearning cardLearning = cardLearningRepository.getCardLearningByUserCardId(userCardId);
            if(cardLearning!=null){
                cardLearning.setHint(cardDto.getHint());
                cardLearningRepository.save(cardLearning);
                return ResponseEntity.status(HttpStatus.CREATED).body("Write Hint successfully");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CardLearning not exist");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Write Hint fail");
        }
    }

    public ResponseEntity listCardByStudySet(Long id) {
        try{
            List<Card> cards = studySetRepository.findById(id).orElse(null).getCards();
            List<CardDto> responses = new ArrayList<>();
            for (Card card: cards) {
                CardDto cardDto = new CardDto();
                cardDto.setId(card.getId());
                cardDto.setStudySet(id);
                cardDto.setBack(card.getBack());
                cardDto.setFront(card.getFront());

                responses.add(cardDto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity setColorCardLearning(Long cardId, String color) {
        try {
            User user = authService.getCurrentAccount().getUser();
            UserCardId userCardId = new UserCardId();
            userCardId.setUserId(user.getId());
            userCardId.setCardId(cardId);
            CardLearning cardLearning = cardLearningRepository.getCardLearningByUserCardId(userCardId);
            if (cardLearning != null) {
                cardLearning.setColor(Color.valueOf(color));
                return ResponseEntity.status(HttpStatus.OK).body("Set Color successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card Learning not exist");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Set Color fail");
        }
    }
}
