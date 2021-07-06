package com.g18.controller;

import com.g18.dto.AuthenticationResponse;
import com.g18.dto.UserResponse;
import com.g18.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/me")
@AllArgsConstructor
public class UserController {
    private final AuthService authService;
    @GetMapping("/about")
    public AuthenticationResponse getUserInfo() {
        AuthenticationResponse authenticationResponse
                = AuthenticationResponse.builder()
                .userResponse(authService.getUserResponseByCurrentAccount(authService.getCurrentAccount()))
                .build();
        return authenticationResponse;
    }

    @GetMapping("/lib")
    public ResponseEntity<String> getUserLib() {
        return new ResponseEntity<>("my lib", HttpStatus.OK);
    }

}

