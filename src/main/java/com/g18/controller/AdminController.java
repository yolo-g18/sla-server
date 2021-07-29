package com.g18.controller;

import com.g18.dto.AuthenticationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    @GetMapping("/reports")
    public ResponseEntity<String> getAllReports() {
        return new ResponseEntity<>("Page admin", HttpStatus.OK);

    }

}
