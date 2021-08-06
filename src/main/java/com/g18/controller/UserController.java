package com.g18.controller;

import com.g18.dto.AuthenticationResponse;
import com.g18.dto.UserProfileDto;
import com.g18.dto.UserResponse;
import com.g18.service.AuthService;
import com.g18.service.UserService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/me")
@AllArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    @GetMapping("/about")
    public AuthenticationResponse getUserInfo() {
        AuthenticationResponse authenticationResponse
                = AuthenticationResponse.builder()
                .userResponse(authService.getUserResponseByCurrentAccount(authService.getCurrentAccount()))
                .roles(authService.getCurrentAccount().getRoles().stream()
                        .map(role -> new String(role.getName().name()))
                        .collect(Collectors.toList()))
                .build();
        return authenticationResponse;
    }

    @GetMapping("/about/{username}")
    public UserResponse getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }


    @PutMapping("")
    public ResponseEntity<Void> updateUserProfile (@Valid  @RequestBody UserProfileDto userProfileDto) throws NotFoundException {
    userService.save(userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/changeFavorTime")
    public ResponseEntity<Void> updateFavorTime(Instant favourTimeFrom, Instant favourTimeTo) {
        userService.updateFavorTime(favourTimeFrom, favourTimeTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/lib")
    public ResponseEntity<String> getUserLib() {
        return new ResponseEntity<>("my lib", HttpStatus.OK);
    }



}

