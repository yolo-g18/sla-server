package com.g18.service;

import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningResponse;
import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import com.g18.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    @Autowired
    private AuthService authService;
    @Autowired
    private StudySetLearningRepository studySetLearningRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SearchService searchService;


    public List<StudySetLearningResponse> getTop6LearningStudySets(){
        List<StudySetLearningResponse> results = new ArrayList<>();
        User currentUser = authService.getCurrentUser();
//        List<StudySetLearning> listLearningSS = studySetLearningRepository.findTop6StudySetLearningByUserId(currentUser.getId());
        Pageable top6 = PageRequest.of(0, 6);
        List<StudySetLearning> listLearningSS = studySetLearningRepository.findTop6StudySetLearningByUserId(currentUser.getId(),top6);
        for(StudySetLearning ssl : listLearningSS){
            StudySetLearningResponse sslDto = new StudySetLearningResponse();
            sslDto.setStudySetId(ssl.getStudySet().getId());
            sslDto.setUserID(ssl.getUser().getId());

            sslDto.setOwner(findUserNameByUserId(studySetRepository.findCreatorIdById(ssl.getStudySet().getId())));

            sslDto.setStudySetName(ssl.getStudySet().getTitle());
            sslDto.setSsDescription(ssl.getStudySet().getDescription());
            sslDto.setColor(ssl.getColor());
            sslDto.setProgress(ssl.getProgress());
            sslDto.setRating(ssl.getRating());
            sslDto.setStatus(ssl.getStatus());
            sslDto.setNumberOfCards(cardRepository.countNumberCardBySSID(ssl.getStudySet().getId()));
            results.add(sslDto);
        }
        return results;
    }

    //
    public List<SearchStudySetResponse> getTop6StudySetCreatedByUserId() {
        User currentUser = authService.getCurrentUser();
        List<StudySet> studySetList =studySetRepository.findTop6ByIsActiveTrueAndCreatorIdOrderByIdDesc(currentUser.getId());
        List<SearchStudySetResponse> result = new ArrayList<>();
        for(StudySet ss : studySetList){
            SearchStudySetResponse sssr = new SearchStudySetResponse();
            sssr.setId(ss.getId());
            sssr.setCreator(findUserNameByUserId(currentUser.getId()));
            sssr.setNumberOfCards(ss.getCards().size());
            sssr.setTitle(ss.getTitle());
            sssr.setDescription(ss.getDescription());
            sssr.setTag(ss.getTag());
            sssr.setFirst4Cards(searchService.convertToCardDto(cardRepository.findTop4ByStudySetId(ss.getId())));
            result.add(sssr);
        }
        return result;
    }

    public String findUserNameByUserId(Long uid){
        return accountRepository.findUserNameByUserId(uid);
    }
}