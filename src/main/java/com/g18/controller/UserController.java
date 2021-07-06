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
                .build();
        return authenticationResponse;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserProfile (@PathVariable("id") long id
            , @Valid  @RequestBody UserProfileDto userProfileDto) throws NotFoundException {
    userService.save(id, userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/lib")
    public ResponseEntity<String> getUserLib() {
        return new ResponseEntity<>("my lib", HttpStatus.OK);
    }

}

