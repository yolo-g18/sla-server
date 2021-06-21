package com.g18.security;

import com.g18.exceptions.SLAException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.sql.Date;

@Service
@Slf4j
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/sla.jks");
            keyStore.load(resourceAsStream, "yolog18".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SLAException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                    .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("sla", "yolog18".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SLAException("Exception occured while retrieving public key from keystore", e);
        }
    }

    public boolean validateToken(String jwt) throws Exception {
        try {
            Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtException("JWT claims string is empty.");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("sla").getPublicKey();
        }catch(KeyStoreException e) {
            throw new SLAException("Exception occured while retrieving" +
                    " public key from keystore", e);
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
