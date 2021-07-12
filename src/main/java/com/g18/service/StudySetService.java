package com.g18.service;

import com.g18.converter.CardConverter;
import com.g18.dto.CardDto;
import com.g18.dto.SearchStudySetResponse;
import com.g18.entity.Card;
import com.g18.entity.StudySet;
import com.g18.repository.CardRepository;
import com.g18.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudySetService {
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardConverter cardConverter;

//    public List<SearchStudySetResponse> searchStudySet(String keyword, Pageable pageable){
//        List<SearchStudySetResponse> results = new ArrayList<>();
//        List<StudySet> listStudySet = studySetRepository.findByTitleContains(keyword,pageable);
//        for (StudySet st : listStudySet){
//            SearchStudySetResponse studySetResponse = new SearchStudySetResponse();
//            studySetResponse.setId(st.getId());
//            studySetResponse.setCreator(st.getCreator().getLastName());
//            studySetResponse.setTitle(st.getTitle());
//            studySetResponse.setNumberOfCards(st.getCards().size());
//            //studySetResponse.setCards(cardRepository.findTop4ByStudySetIdInOrderByInsertDateDesc(st.getId()));
//            List<Card> listCardFound = cardRepository.findTop4ByStudySetId(st.getId());
//            List<CardDto> listCardDto = new ArrayList<>();
//            for (Card card : listCardFound){
//                CardDto cardDto = cardConverter.toDto(card);
//                listCardDto.add(cardDto);
//            }
//
//            studySetResponse.setCards(listCardDto);
//            results.add(studySetResponse);
//        }
//        return results;
//    }

    public int totalItem(){
        return (int) studySetRepository.count();
    }

}