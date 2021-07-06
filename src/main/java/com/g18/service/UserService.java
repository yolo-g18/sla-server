package com.g18.service;

import com.g18.dto.UserProfileDto;
import com.g18.entity.Account;
import com.g18.entity.User;
import com.g18.exceptions.AccountException;
import com.g18.repository.AccountRepository;
import com.g18.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;

    public void save(Long id, UserProfileDto userProfileDto) throws NotFoundException{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found user with id"));
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Not found accout with user id"));
        if(authService.checkEmailExistence(userProfileDto.getEmail()) && !userProfileDto.getEmail().equals(user.getEmail())){
            throw new AccountException("Email is already taken");
        }
        user.setFirstName(userProfileDto.getFirstname());
        user.setLastName(userProfileDto.getLastname());
        user.setAddress(userProfileDto.getAddress());
        user.setEmail(userProfileDto.getEmail());
        user.setAvatar(userProfileDto.getAvatar());
        user.setJob(userProfileDto.getJob());
        user.setSchoolName(userProfileDto.getSchoolName());
        user.setEmail(userProfileDto.getEmail());
        user.setBio(userProfileDto.getBio());
        user.setMajor(userProfileDto.getMajor());

        account.setUpdateDate(Instant.now());

        userRepository.save(user);
        accountRepository.save(account);
    }




}
