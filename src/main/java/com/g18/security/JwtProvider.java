package com.g18.security;

import com.g18.exceptions.SLAException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try{
            keyStore = KeyStore.getInstance("JSK`");
            InputStream resourceAsStream = getClass().getResourceAsStream("/sla.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        }catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SLAException("Exception occurred while loading keystore", e);
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("sla").getPublicKey();
        } catch(KeyStoreException ex) {
            throw new SLAException("Exception occurred while " +
                    "retrieving public key from keystore");
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

}
