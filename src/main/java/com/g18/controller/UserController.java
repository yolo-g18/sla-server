package com.g18.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/me")
@AllArgsConstructor
public class UserController {
    @GetMapping("/about")
    public ResponseEntity<String> verifyAccount() {
        return new ResponseEntity<>("about me!", HttpStatus.OK);
    }
}

///lib
