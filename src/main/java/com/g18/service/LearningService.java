package com.g18.service;


import com.g18.dto.CardLearningDto;
import com.g18.dto.CardQualityRequestUpdate;
import com.g18.entity.*;

import com.g18.model.Status;
import com.g18.model.UserCardId;
import com.g18.model.UserStudySetId;
import com.g18.repository.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class LearningService {

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

    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity learningFlashCardByStudySet(Long studySetId){
        // TODO Auto-generated method stub
        List<CardLearningDto> responses = new ArrayList<>();
        try{
            User user = authService.getCurrentAccount().getUser();
            StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(() -> new ExpressionException("Study Set not exist"));
            List<Card> cards = studySet.getCards();
            int numberOfCardAddNew = 0;
            for (Card card: cards) {
                CardLearning cardLearning = cardLearningRepository.findCardLearningByCardAndUser(card,user);
                if(cardLearning == null){
                    numberOfCardAddNew++;
                    //Set User-Card
                    UserCardId userCardId = new UserCardId();
                    userCardId.setUserId(user.getId());
                    userCardId.setCardId(card.getId());

                    //Set CardLearning to Insert DB
                    cardLearning = new CardLearning();
                    cardLearning.setUserCardId(userCardId);
                    cardLearning.setCard(card);
                    cardLearning.setUser(user);

                    Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
                    cardLearning.setLearnedDate(now);

                    cardLearning.setColor(null);
                    cardLearning.setEFactor(2.5);
                    cardLearning.setHint(null);
                    cardLearning.setIntervalTime(0);
                    cardLearning.setQ(0);
                    cardLearning.setStatus(Status.NOTSTARTED);

                    cardLearningRepository.save(cardLearning);
                }
                //Set CardLearningDto to response
                CardLearningDto cardLearningDto = convertCardLearningToDTO(cardLearning);
                responses.add(cardLearningDto);
            }
            int numberOfCard = cards.size();
            StudySetLearning studySetLearning = new StudySetLearning();
            boolean isFirstTime = false;
            if(numberOfCardAddNew != 0) {
                if (numberOfCardAddNew == numberOfCard) {
                    isFirstTime = true;
                    //Set StudySetLearning to Insert DB
                    UserStudySetId userStudySetId = new UserStudySetId();
                    userStudySetId.setStudySetId(studySetId);
                    userStudySetId.setUserId(user.getId());
                    studySetLearning.setUserStudySetId(userStudySetId);
                    studySetLearning.setStudySet(studySet);
                    studySetLearning.setUser(user);
                    studySetLearning.setColor(null);
                    studySetLearning.setExpectedDate(null);
                    studySetLearning.setFeedback(null);
                    studySetLearning.setProgress(0);
                    studySetLearning.setRating(0);
                    studySetLearning.setStartDate(Instant.now());
                    studySetLearning.setStatus(Status.LEARNING);
                    studySetLearning.setPublic(studySet.isPublic());

                } else {
                    StudySetLearning ssl = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet, user);

                    double progress = ssl.getProgress();
                    progress = progress * (numberOfCard - numberOfCardAddNew) / numberOfCard;
                    studySetLearning.setProgress(progress);
                }
                studySetLearningRepository.save(studySetLearning);
            }
            //Add EventLearning if user learning for the first time
            if(isFirstTime){
                Instant now = Instant.now();

                //Convert date to String
                Date myDate = Date.from(now);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(myDate);

                //Query using Like ->
                List<Event> eventListToday = eventRepository.getListEventByUserIdAndDate(user.getId(), today);

                //If there was another event before
                if(eventListToday != null) {
                    //Check if exist event learn of StudySet before
                    boolean isExistEventOfStudySet = false;

                    for(Event eventToday : eventListToday){
                        String[] split = eventToday.getDescription().split("\\|");
                        Long studySetIdOfEventBefore = Long.valueOf(split[1].trim());
                        if(studySetIdOfEventBefore == studySetId) {
                            isExistEventOfStudySet = true;
                            break;
                        }
                    }
                    //If different studySetID Of Event Before -> Create New Event
                    if(!isExistEventOfStudySet){
                        Event eventLearning = new Event();
                        eventLearning.setUser(user);

                        eventLearning.setDescription("Learn |" + studySetId);

                        //Set time learn
                        Instant from = now.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();
                        eventLearning.setFromTime(from);

                        Instant to = now.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();
                        eventLearning.setToTime(to);

                        eventLearning.setColor(null);

                        String dayOfWeek = now.atZone(ZoneId.systemDefault()).getDayOfWeek().toString();

                        eventLearning.setName("Learning event on " + dayOfWeek);
                        eventLearning.setCreatedTime(now);
                        eventLearning.setUpdateTime(now);
                        eventLearning.setLearnEvent(true);
                        eventRepository.save(eventLearning);
                    }
                }else{
                    Event eventLearning = new Event();
                    eventLearning.setUser(user);

                    eventLearning.setDescription("Learn |" + studySetId);

                    Instant from = now.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();
                    eventLearning.setFromTime(from);

                    Instant to = now.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();
                    eventLearning.setToTime(to);

                    eventLearning.setColor(null);

                    String dayOfWeek = now.atZone(ZoneId.systemDefault()).getDayOfWeek().toString();

                    eventLearning.setName("Learning event on " + dayOfWeek);
                    eventLearning.setCreatedTime(now);
                    eventLearning.setUpdateTime(now);
                    eventRepository.save(eventLearning);
                }
            }
        }catch (Exception e){
            log.info("learningFlashCardByStudySet Exception: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    public ResponseEntity learningFlashCardToday() {
        // TODO Auto-generated method stub
        List<CardLearningDto> responses = new ArrayList<>();

        try{
            User user = authService.getCurrentUser();
            Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);

            List<CardLearning> cardLearningList = cardLearningRepository.findByUserAndLearnedDate(user,now);

            for(CardLearning cardLearning : cardLearningList){

                CardLearningDto cardLearningDto = convertCardLearningToDTO(cardLearning);

                responses.add(cardLearningDto);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        }catch (Exception e){
            log.info("learningFlashCardToday Exception: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity updateCardLearning(CardQualityRequestUpdate cardQualityRequestUpdate) {
        try{
            User user = authService.getCurrentUser();
            Card card = cardRepository.findById(cardQualityRequestUpdate.getCardId()).orElseThrow(() -> new ExpressionException("Card not exist"));

            //Get cardLearning to update
            CardLearning cardLearning = cardLearningRepository.findCardLearningByCardAndUser(card,user);
            if(cardLearning != null){
                //Get infor
                double eFactor = cardLearning.getEFactor();
                double interval = cardLearning.getIntervalTime();
                Integer qualityBefore = cardLearning.getQ();

                Integer repetitionNumber = cardLearning.getRepetitionNumber();
                Status status;

                //If quality >=3, update interval, eFactor, repetitionNumber, status
                if(cardQualityRequestUpdate.getQ() >= 3){
                    status = Status.REVIEW;
                    if(repetitionNumber == 0){
                        interval = 1;
                    }else if(repetitionNumber == 1){
                        interval = 6;
                    }else{
                        interval = interval * eFactor;
                    }
                    eFactor = eFactor + (0.1 - (5 - cardQualityRequestUpdate.getQ()) * (0.08 + (5 - cardQualityRequestUpdate.getQ()) * 0.02));
                    if(eFactor < 1.3){
                        eFactor = 1.3;
                    }
                    repetitionNumber++;

                    //If qualityBerore < 3, qualityUpdate >=3 -> Update progress increase(+)
                    if(qualityBefore < 3){
                        //Update progress in StudySetLearning
                        StudySet studySet = studySetRepository.findById(card.getStudySet().getId()).orElseThrow(()-> new ExpressionException("Study Set not exist"));
                        StudySetLearning studySetLearning = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet, user);
                        double progress = studySetLearning.getProgress();
                        int numberOfCard = studySet.getCards().size();
                        progress = progress + 1.0/numberOfCard;
                        if(progress == 1){
                            studySetLearning.setExpectedDate(Instant.now());
                            studySetLearning.setStatus(Status.FINISHED);
                        }
                        studySetLearning.setProgress(progress);
                        studySetLearningRepository.save(studySetLearning);
                    }
                }else{
                    status = Status.LEARNING;
                    repetitionNumber = 0;
                    interval = 1;

                    //If qualityBefore >= 3, qualityUpdate < 3 -> Update progress decrease(-)
                    if(qualityBefore >= 3){
                        StudySet studySet = studySetRepository.findById(card.getStudySet().getId()).orElseThrow(()-> new ExpressionException("Study Set not exist"));
                        StudySetLearning studySetLearning = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet, user);
                        double progress = studySetLearning.getProgress();
                        if(progress == 1){
                            studySetLearning.setExpectedDate(null);
                            studySetLearning.setStatus(Status.REVIEW);
                        }
                        int numberOfCard = studySet.getCards().size();
                        progress = progress - 1.0/numberOfCard;
                        studySetLearning.setProgress(progress);
                        studySetLearningRepository.save(studySetLearning);
                    }
                }

                Instant now = Instant.now();
                BigDecimal intervalRounding = new BigDecimal(interval).setScale(0,RoundingMode.DOWN);
                Instant learnDate = now.plus(intervalRounding.intValue(), ChronoUnit.DAYS);

                //Update CardLearning DB
                cardLearning.setQ(cardQualityRequestUpdate.getQ());
                cardLearning.setLearnedDate(learnDate);
                cardLearning.setIntervalTime(intervalRounding.intValue());
                cardLearning.setEFactor(eFactor);
                cardLearning.setRepetitionNumber(repetitionNumber);
                cardLearning.setStatus(status);
                cardLearningRepository.save(cardLearning);

                //Update Event
                Date dateConvert = Date.from(learnDate);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(dateConvert);
                Long studySetIdOfCard = card.getStudySet().getId();

                List<Event> eventListToday = eventRepository.getListEventByUserIdAndDate(user.getId(), date);
                if(eventListToday != null){
                    //Check if exist event learn of StudySet before
                    boolean isExistEventOfStudySet = false;

                    for(Event eventToday : eventListToday){
                        String[] split = eventToday.getDescription().split("\\|");
                        Long studySetIdOfEventBefore = Long.valueOf(split[1].trim());
                        if(studySetIdOfEventBefore == studySetIdOfCard) {
                            isExistEventOfStudySet = true;
                            break;
                        }
                    }
                    if(!isExistEventOfStudySet){
                        Event eventLearning = new Event();
                        eventLearning.setUser(user);

                        eventLearning.setDescription("Learn |" + studySetIdOfCard);

                        Instant from = learnDate.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();
                        eventLearning.setFromTime(from);

                        Instant to = learnDate.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();
                        eventLearning.setToTime(to);

                        eventLearning.setColor(null);

                        String dayOfWeek = now.atZone(ZoneId.systemDefault()).getDayOfWeek().toString();

                        eventLearning.setName("Learning event on " + dayOfWeek);
                        eventLearning.setCreatedTime(now);
                        eventLearning.setUpdateTime(now);
                        eventLearning.setLearnEvent(true);
                        eventRepository.save(eventLearning);
                    }
                }else{
                    Event eventLearning = new Event();
                    eventLearning.setUser(user);

                    eventLearning.setDescription("Learn |" + studySetIdOfCard);

                    Instant from = learnDate.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toInstant();
                    eventLearning.setFromTime(from);

                    Instant to = learnDate.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toInstant();
                    eventLearning.setToTime(to);

                    eventLearning.setColor(null);

                    String dayOfWeek = now.atZone(ZoneId.systemDefault()).getDayOfWeek().toString();

                    eventLearning.setName("Learning event on " + dayOfWeek);
                    eventLearning.setCreatedTime(now);
                    eventLearning.setUpdateTime(now);
                    eventRepository.save(eventLearning);

                }
                return ResponseEntity.status(HttpStatus.OK).body("Update after learning successfully");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Card for learning has not been created");
            }
        }catch(Exception e){
            log.info("updateCardLearning Exception: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    CardLearningDto convertCardLearningToDTO(CardLearning cardLearning){
        CardLearningDto cardLearningDto = new CardLearningDto();
        cardLearningDto.setCardId(cardLearning.getCard().getId());
        cardLearningDto.setUserId(cardLearning.getUser().getId());
        cardLearningDto.setStudySetId(cardLearning.getCard().getStudySet().getId());
        cardLearningDto.setFront(cardLearning.getCard().getFront());
        cardLearningDto.setBack(cardLearning.getCard().getBack());
        cardLearningDto.setColor(cardLearning.getColor());
        cardLearningDto.setHint(cardLearning.getHint());
        return cardLearningDto;
    }

    public ResponseEntity learningContinue(Long studySetId) {
        User user = authService.getCurrentAccount().getUser();
        List<CardLearningDto> response = new ArrayList<>();
        StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(()->new ExpressionException("Study Set not exist"));

        List<Card> cardList = studySet.getCards();

        for(Card card : cardList){
            if(card.getStudySet().equals(studySet)){
                for(CardLearning cardLearning: card.getCardLearningList()){
                    if(cardLearning.getUser().equals(user)){
                        CardLearningDto cardLearningDto = convertCardLearningToDTO(cardLearning);

                        response.add(cardLearningDto);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
