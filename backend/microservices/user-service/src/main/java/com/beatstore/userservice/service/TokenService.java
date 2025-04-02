package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.exception.TokenExpiredException;
import com.beatstore.userservice.exception.TokenNotFoundException;
import com.beatstore.userservice.model.RefreshToken;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.RefreshTokenRepository;
import com.beatstore.userservice.repository.UserAccountRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long jwtExpirationMs = 3600000; // 1 godzina

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository, UserAccountRepository userAccountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public String generateJwtToken(UserDTO userDetails) {
        return Jwts.builder()
                .subject(userDetails.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(UserDTO userDetails) {
        String token = UUID.randomUUID().toString();
        UserAccount userAccount = UserAccount.builder()
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .build();
        RefreshToken refreshToken = new RefreshToken(token, LocalDateTime.now().plusWeeks(1), userAccount);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public UserDTO verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            throw new TokenExpiredException("Refresh token has expired");
        }
        UserAccount userAccount = refreshToken.getUserAccount();
        return UserDTO.builder()
                .email(userAccount.getEmail())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .build();
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    }
}
