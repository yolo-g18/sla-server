package com.g18.repository;

import com.g18.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByAccount_Id(Long id);

    void deleteByToken(String token);
    void deleteByAccount_Id(Long id);
}
