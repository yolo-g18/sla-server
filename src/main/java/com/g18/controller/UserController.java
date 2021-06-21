package com.g18.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/me")
@AllArgsConstructor
public class UserController {
    @GetMapping("/about")
    public ResponseEntity<String> getUserInfo() {
            return new ResponseEntity<>("about me!", HttpStatus.OK);
    }

    @GetMapping("/lib")
    public ResponseEntity<String> getUserLib() {
        return new ResponseEntity<>("my lib", HttpStatus.OK);
    }

}

