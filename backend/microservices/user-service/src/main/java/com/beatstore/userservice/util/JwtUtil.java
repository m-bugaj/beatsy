package com.beatstore.userservice.util;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = "kfnjerfnj4f3nj4u5847g03573gh53gb43b";

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 + 24))
                .signWith(getSecretKey())
                .compact();
    }

    // Walidacja tokenu
    public boolean validateToken(String token, String username) {
        String user = extractUsername(token);
        return (user.equals(username) && !isTokenExpired(token));
    }

    // Ekstrakcja nazwy użytkownika z tokenu
    public String extractUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Sprawdzanie, czy token wygasł
    private boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    }
}
