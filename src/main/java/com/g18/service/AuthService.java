package com.g18.service;

import com.g18.dto.*;
import com.g18.entity.*;
import com.g18.exceptions.AccountException;
import com.g18.exceptions.SLAException;
import com.g18.model.ERole;
import com.g18.model.NotificationEmail;
import com.g18.repository.AccountRepository;
import com.g18.repository.RoleRepository;
import com.g18.repository.UserRepository;
import com.g18.repository.VerificationTokenRepository;
import com.g18.security.JwtProvider;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${admin.email}")
    private String adminEmail;

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

            //init role

            if(!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
                Role role = new Role();
                role.setName(ERole.ROLE_USER);
                roleRepository.save(role);
            }
            if(!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
                Role role = new Role();
                role.setName(ERole.ROLE_ADMIN);
                roleRepository.save(role);
            }
            Set<Role> roles = new HashSet<>();
            //check email have permi register as role admin
            if(registerRequest.getEmail().equals(adminEmail)) {
                Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            account.setRoles(roles);

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

    public boolean checkEmailExistence(String email) {
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
        Optional<RefreshToken> refreshToken = refreshTokenService.getRefreshTokenByAccountId(account.getId());

        if(refreshToken.isPresent()) {
            refreshTokenService.deleteRefreshTokenByAccountId(account.getId());
        }
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtProvider.generateToken(authenticate);

            log.error(String.valueOf(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))));

            UserResponse userResponse = getUserResponseByCurrentAccount(getCurrentAccount());


            org.springframework.security.core.userdetails.User userDetails
                    = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken(loginRequest.getUsername()).getToken())
                    .expiresAt(String.valueOf(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))))
                    .userResponse(userResponse)
                    .roles(roles)
                    .build();
        }catch (Exception ex) {
            throw new AccountException("Incorrect password.");
        }
    }

    public UserResponse getUserResponseByCurrentAccount(Account account) {
        User user = account.getUser();
        log.error("firstname: " + user.getFirstName());
        log.error("lastname: " + user.getLastName());
        try{
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

        }catch (Exception ex) {
            throw new SLAException("Occur error");
        }
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

    public void changePassword (String newPassword, String reNewPassword, String oldPassword) {

        Account account = getCurrentAccount();
        if(account.getPassword() != passwordEncoder.encode(oldPassword)) {
            throw new AccountException("Wrong Password, Please enter again!");
        } else {
            if(newPassword != reNewPassword) {
                throw new AccountException("Password not match");
            } else {
                account.setPassword(passwordEncoder.encode((newPassword)));
                accountRepository.save(account);
            }

        }

    }




}
