package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.UserProfileDto;
import com.g18.dto.UserResponse;
import com.g18.entity.Account;
import com.g18.entity.Notification;
import com.g18.entity.User;
import com.g18.exceptions.AccountException;
import com.g18.repository.AccountRepository;
import com.g18.repository.NotificationRepository;
import com.g18.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    public void save(UserProfileDto userProfileDto) throws NotFoundException{

        Account account = authService.getCurrentAccount();
        User user = account.getUser();
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

    public UserResponse getUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found"));
        User user = account.getUser();
        UserResponse userResponse = UserResponse.builder()
                ._id(user.getId())
                .username(account.getUsername())
                .firstname(account.getUser().getFirstName())
                .lastname(account.getUser().getLastName())
                .avatar(user.getAvatar())
                .job(user.getJob())
                .email(user.getEmail())
                .bio(account.getUser().getBio())
                .major(account.getUser().getMajor())
                .address(user.getAddress())
                .schoolName(user.getSchoolName())
                .createdAt(String.valueOf(account.getCreatedDate()))
                .updatedAt(String.valueOf(account.getUpdateDate()))
                .favourTimeFrom(String.valueOf(user.getFavourTimeFrom()))
                .favourTimeTo(String.valueOf(user.getFavourTimeTo()))
                .build();

        return userResponse;
    }

    public String getUserNameOfPerson(Long creator_id){

        List<Account> accountList = accountRepository.findAll();

        // find account of creator
        Account acc = accountList.stream().filter( account -> account.getUser().getId().equals(creator_id))
                .findAny().orElse(null);

        return acc.getUsername();
    }

    public void updateFavorTime(ObjectNode json) {
//        try {
//            if(favourTimeTo.compareTo(favourTimeFrom) < 0) {
//                throw new AccountException("Invalid time");
//            } else {
//                User user = authService.getCurrentUser();
//                user.setFavourTimeFrom(favourTimeFrom);
//                user.setFavourTimeTo(favourTimeTo);
//
//                userRepository.save(user);
//            }
//        }catch (Exception err) {
//            log.error(String.valueOf(err));
//        }
        Instant favourTimeFrom = Instant.parse(json.get("favourTimeFrom").asText());
        Instant favourTimeTo = Instant.parse(json.get("favourTimeTo").asText());
        if(favourTimeTo.compareTo(favourTimeFrom) < 0) {
            throw new AccountException("Invalid time");
        } else {
            User user = authService.getCurrentUser();
            user.setFavourTimeFrom(favourTimeFrom);
            user.setFavourTimeTo(favourTimeTo);

            userRepository.save(user);

            String type = "learn";
            Notification notification = notificationRepository.findNotificationByUserAndType(user, type);
            if(notification!=null) {

                if (favourTimeFrom.isBefore(Instant.now())) {
                    favourTimeFrom = favourTimeFrom.plus(1, ChronoUnit.DAYS);
                }
                notification.setTimeTrigger(favourTimeFrom.truncatedTo(ChronoUnit.MINUTES));

                notificationRepository.save(notification);
            }else{
                Notification notificationNew = new Notification();
                notificationNew.setUser(user);
                notificationNew.setTitle("Time to learn");
                notificationNew.setDescription("Time to learn");
                notificationNew.setType(type);
                notificationNew.setLink(null);
                notificationNew.setCreatedTime(Instant.now());

                if (favourTimeFrom.isBefore(Instant.now())) {
                    favourTimeFrom = favourTimeFrom.plus(1, ChronoUnit.DAYS);
                }
                notificationNew.setTimeTrigger(favourTimeFrom.truncatedTo(ChronoUnit.MINUTES));
                notificationNew.setRead(true);
                notificationRepository.save(notificationNew);
            }
        }

    }



}