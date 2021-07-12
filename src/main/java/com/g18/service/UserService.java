package com.g18.service;

import com.g18.dto.SearchUserResponse;
import com.g18.entity.Account;
import com.g18.repository.AccountRepository;
import com.g18.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private AccountRepository  accountRepository;
    @Autowired
    private StudySetRepository studySetRepository;

    public List<SearchUserResponse> searchUser(String keyword){
        List<SearchUserResponse> results = new ArrayList<>();

        List<Account> listAccount = accountRepository.findByUsernameContains(keyword);

        for (Account acc : listAccount){
            SearchUserResponse sur = new SearchUserResponse();
            sur.setAvatar(acc.getUser().getAvatar());
            sur.setId(acc.getUser().getId());//this is user id not account id
            sur.setUsername(acc.getUsername());
            sur.setNumberStudySetOwn(studySetRepository.findByCreatorId(acc.getUser().getId()).size());
            results.add(sur);
        }


        return results;
    }

}
