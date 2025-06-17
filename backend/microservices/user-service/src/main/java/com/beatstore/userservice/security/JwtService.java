package com.beatstore.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beatstore.userservice.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    private final Algorithm algorithm;

    public JwtService(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String generateToken(UserAccount userAccount) {
        return JWT.create()
                .withSubject(userAccount.getUsername())
                .withClaim("userHash", userAccount.getUserHash())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }


//    // Walidacja tokenu
//    public boolean validateToken(String token, String username) {
//        String user = extractUsername(token);
//        return (user.equals(username) && !isTokenExpired(token));
//    }
//
//    // Ekstrakcja nazwy użytkownika z tokenu
//    public String extractUsername(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//
//        return Jwts.parser()
//                .verifyWith(getSecretKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    // Sprawdzanie, czy token wygasł
//    private boolean isTokenExpired(String token) {
//        return getAllClaimsFromToken(token).getExpiration().before(new Date());
//    }
//
//    private SecretKey getSecretKey() {
//        return new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//    }
}
