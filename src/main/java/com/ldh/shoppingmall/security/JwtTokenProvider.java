package com.ldh.shoppingmall.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "MySuperSecretKeyForJwtToken123456789012345";
    private static final long EXPIRATION_TIME = 86400000; // 24 hrs

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // HMAC SHA key

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName()) // save username
                .setIssuedAt(new Date()) // created date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expired date
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (MalformedJwtException e) {
            log.error("JWT token is corrupted.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claim is Empty.");
        }
        return false;
    }
}
