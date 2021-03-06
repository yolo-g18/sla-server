package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.*;
import com.g18.entity.Account;
import com.g18.service.AuthService;
import com.g18.service.RefreshTokenService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Register Successful", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public String verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return "activeAcc";
    }

    @PostMapping("/login")
    @ResponseBody
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    @ResponseBody
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        return authService.refreshToken(refreshTokenRequest);
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout (HttpServletRequest request, HttpServletResponse response) {
        Account account = authService.getCurrentAccount();
        log.error("acc id: " + account.getId());
        refreshTokenService.deleteRefreshTokenByAccountId(account.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Delete Successfully!!");
    }

    @PostMapping("/forgotPassword")
    @ResponseBody
    public String forgotPassword(@RequestBody ObjectNode json){
        return authService.resetNewPasswordForUser(json);
    }



}
