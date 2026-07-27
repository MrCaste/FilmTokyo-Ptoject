package com.filmtokio.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.filmtokio.Config.SecurityConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtRestService {

    private final SecurityConfig securityConfig;
    private SecretKey secretKey;

    public String generateJwtToken(String clientId, long expirationInSeconds) {

        final Date now = new Date();

        return Jwts.builder()
                .subject(clientId)
                .claim("client_id", clientId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationInSeconds * 1000))
                .signWith(secretKey)
                .compact();

    }

    public String extractSubject(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(securityConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

}
