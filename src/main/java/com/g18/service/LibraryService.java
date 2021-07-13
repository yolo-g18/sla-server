package com.g18.service;

import com.g18.dto.SearchStudySetResponse;
import com.g18.dto.StudySetLearningDto;
import com.g18.entity.Account;
import com.g18.entity.StudySet;
import com.g18.entity.StudySetLearning;
import com.g18.entity.User;
import com.g18.repository.AccountRepository;
import com.g18.repository.StudySetLearningRepository;
import com.g18.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    @Autowired
    private AuthService authService;
    @Autowired
    private StudySetLearningRepository studySetLearningRepository;
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private AccountRepository accountRepository;


    //get all study sets current user have created
    public List<StudySetLearningDto> getLearningStudySets(){
        List<StudySetLearningDto> results = new ArrayList<>();
        User currentUser = authService.getCurrentUser();
        List<StudySetLearning> listLearningSS = studySetLearningRepository.findStudySetLearningByUserId(currentUser.getId());
        for(StudySetLearning ssl : listLearningSS){
            StudySetLearningDto sslDto = new StudySetLearningDto();
            sslDto.setStudySetId(ssl.getStudySet().getId());
            sslDto.setStudySetName(ssl.getStudySet().getTitle());
            sslDto.setSsDescription(ssl.getStudySet().getDescription());
            sslDto.setColor(ssl.getColor());
            sslDto.setProgress(ssl.getProgress());
            sslDto.setRating(ssl.getRating());
            sslDto.setStatus(ssl.getStatus());
            results.add(sslDto);
        }
        return results;
    }
    // get all study sets the current user have created
    public List<SearchStudySetResponse> getStudySetCreatedByUserId(long userId) {
        User currentUser = authService.getCurrentUser();
        List<StudySet> studySetList =studySetRepository.findByCreatorId(userId);
        List<SearchStudySetResponse> result = new ArrayList<>();
        for(StudySet ss : studySetList){
            SearchStudySetResponse sssr = new SearchStudySetResponse();
            sssr.setId(ss.getId());
            sssr.setCreator(findUserNameByUserId(currentUser.getId()));
            sssr.setNumberOfCards(ss.getCards().size());
            sssr.setTitle(ss.getTitle());
            result.add(sssr);
        }
        return result;
    }









    //
    public String findUserNameByUserId(long uid){
        return accountRepository.findUserNameByUserId(uid);
    }


}
