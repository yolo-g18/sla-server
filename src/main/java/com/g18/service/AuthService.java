package com.g18.service;

import com.g18.dto.*;
import com.g18.entity.Account;
import com.g18.entity.RefreshToken;
import com.g18.entity.User;
import com.g18.entity.VerificationToken;
import com.g18.exceptions.AccountException;
import com.g18.exceptions.SLAException;
import com.g18.model.NotificationEmail;
import com.g18.repository.AccountRepository;
import com.g18.repository.UserRepository;
import com.g18.repository.VerificationTokenRepository;
import com.g18.security.JwtProvider;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup( RegisterRequest registerRequest) {
        if (checkEmailExistence(registerRequest.getEmail()) && checkUsernameExistence(registerRequest.getUsername())) {
            throw new AccountException("Email and username are already taken");
        }else if(checkEmailExistence(registerRequest.getEmail())){
            throw new AccountException("Email is already taken");
        } else if(checkUsernameExistence(registerRequest.getUsername())) {
            throw new AccountException("Username is already taken");
        } else {
            User user = new User();
            Account account = new Account();

            user.setEmail(registerRequest.getEmail());

            account.setUsername(registerRequest.getUsername());
            account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            account.setActive(false);
            account.setUser(user);
            account.setCreatedDate(Instant.now());
            account.setUpdateDate(Instant.now());

            userRepository.save(user);
            accountRepository.save(account);
            String token = generateVerificationToken(account);

            //send mail for
            mailService.sendMail(new NotificationEmail("Please Active your Account", user.getEmail(),
                    "Thank you for signing up SLS, " +
                            "please click on the below url to activate your account: " +
                            "http://localhost:8080/api/auth/accountVerification/" + token));
        }
    }

    private boolean checkEmailExistence(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    private boolean checkUsernameExistence(String username){
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.isPresent();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
        return account.getUser();
    }

    public Account getCurrentAccount() {
        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
        return account;
    }

    private String generateVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getAccount().getUsername();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AccountException("Account not found with name - " + username));
        account.setActive(true);
        accountRepository.save(account);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SLAException("Invalid Token")));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws NotFoundException {
        //check refresh token existance in db to delete
        Account account = accountRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("Account not found with name " + loginRequest.getUsername()));
        UserResponse userResponse = getUserResponseByCurrentAccount(account);
        Optional<RefreshToken> refreshToken = refreshTokenService.getRefreshTokenByAccountId(account.getId());
        if(refreshToken.isPresent()) {
            refreshTokenService.deleteRefreshTokenByAccountId(account.getId());
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        log.error(String.valueOf(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))));

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken(loginRequest.getUsername()).getToken())
                .expiresAt(String.valueOf(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))))
                .userResponse(userResponse)
                .build();
    }

    public UserResponse getUserResponseByCurrentAccount(Account account) {
        User user = account.getUser();
        log.error("firstname: " + user.getFirstName());
        log.error("lastname: " + user.getLastName());
        try{
            UserResponse userResponse = UserResponse.builder()
                    ._id(user.getId())
                    .username(account.getUsername())
                    .fullname((user.getFirstName() != null && user.getLastName() != null) ? user.getFirstName() + " " + user.getLastName() : null)
                    .avatar(user.getAvatar())
                    .job(user.getJob())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .schoolName(user.getSchoolName())
                    .createdAt(account.getCreatedDate())
                    .updatedAt(account.getUpdateDate())
                    .favourTimeFrom(user.getFavourTimeFrom())
                    .favourTimeTo(user.getFavourTimeTo())
                    .build();
            return userResponse;

        }catch (Exception ex) {
            log.error(String.valueOf("dasd" + ex));
        }
        return null;

    }

    public AuthenticationResponse refreshToken (RefreshTokenRequest refreshTokenRequest) throws Exception{
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        Account account = accountRepository.findByUsername(refreshTokenRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("Account not found with name " + refreshTokenRequest.getUsername()));
        User user = account.getUser();

        UserResponse userResponse = getUserResponseByCurrentAccount(account);

        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(String.valueOf(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))))
                .userResponse(userResponse)
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }




}
