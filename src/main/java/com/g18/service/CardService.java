package com.g18.service;

import com.g18.dto.CardDto;
import com.g18.dto.StudySetResponse;
import com.g18.entity.Card;
import com.g18.entity.CardLearning;
import com.g18.entity.StudySet;

import com.g18.repository.CardLearningRepository;
import com.g18.repository.CardRepository;
import com.g18.repository.StudySetRepository;
import com.g18.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class CardService {

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

    public String createCard(List<CardDto> request) {
        try{
            StudySet studySet = studySetRepository.findById(request.get(0).getStudySet()).orElse(null);
            for (CardDto cardDto: request) {
                Card card = new Card();
                card.setStudySet(studySet);
                card.setBack(cardDto.getBack());
                card.setFront(cardDto.getFront());
                cardRepository.save(card);
            }
            return "add Card successfully";
        }catch (Exception e){
            e.printStackTrace();
            return "add Card fail";
        }
    }

    public String editCard(List<CardDto> request) {

        try{
            StudySet studySet = studySetRepository.findById(request.get(0).getStudySet()).orElse(null);
            for (CardDto cardDto: request) {
                Card card = new Card();
                card.setId(cardDto.getId());
                card.setStudySet(studySet);
                card.setBack(cardDto.getBack());
                card.setFront(cardDto.getFront());
                cardRepository.save(card);
            }
            return "edit Card successfully";
        }catch (Exception e){
            e.printStackTrace();
            return "edit Card fail";
        }
    }

    public String deleteCard(Long id) {
        cardRepository.deleteById(id);
        return "delete Card successfully";
    }


    public String writeHint(CardDto cardDto) {
        try{
            CardLearning cardLearning = cardLearningRepository.findById(cardDto.getId()).orElse(null);

            cardLearning.setHint(cardDto.getHint());
            cardLearningRepository.save(cardLearning);
            return "write Hint successfully";
        }catch (Exception e){
                return "write Hint fail";
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
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
