package com.g18.service;

import com.g18.entity.Account;
import com.g18.entity.RefreshToken;
import com.g18.exceptions.AccountException;
import com.g18.exceptions.SLAException;
import com.g18.repository.AccountRepository;
import com.g18.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;


    public RefreshToken generateRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() ->  new AccountException("Account not found with name - " + username));
        refreshToken.setAccount(account);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> getRefreshTokenByAccountId(Long id) {
        return refreshTokenRepository.findByAccount_Id(id);
    }

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SLAException("Invalid refresh Token"));
    }


    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
    public void deleteRefreshTokenByAccountId(Long id) {
        refreshTokenRepository.deleteByAccount_Id(id);
    }
}
