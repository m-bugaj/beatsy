package com.beatstore.authservice.security;

import com.beatstore.authservice.exception.TokenExpiredException;
import com.beatstore.authservice.exception.TokenNotFoundException;
import com.beatstore.authservice.model.RefreshToken;
import com.beatstore.authservice.model.UserAccount;
import com.beatstore.authservice.repository.RefreshTokenRepository;
import com.beatstore.authservice.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

//    @Value("${jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserAccountRepository userAccountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userAccountRepository = userAccountRepository;
    }

//    public String generateJwtToken(UserAccount userAccount) {
//        return Jwts.builder()
//                .subject(userAccount.getEmail())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(getSecretKey())
//                .compact();
//    }

    public String generateRefreshToken(UserAccount userAccount) {
        String token = UUID.randomUUID().toString();
//        UserAccount userAccount = userAccountRepository.findByUsername(userDetails.getUsername())
//                .or(() -> userAccountRepository.findByEmail(userDetails.getEmail())) // jeśli nie ma po username, szuka po emailu
//                .orElseThrow(() -> {
//                    log.warn("User not found: {}", userDetails.getEmail());
//                    return new UsernameNotFoundException("User not found with email: " + userDetails.getEmail());
//                });

//        UserAccount userAccount = UserAccount.builder()
//                .email(userDetails.getEmail())
//                .firstName(userDetails.getFirstName())
//                .lastName(userDetails.getLastName())
//                .build();
        RefreshToken refreshToken = new RefreshToken(token, LocalDateTime.now().plusWeeks(1), userAccount);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public UserAccount verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            throw new TokenExpiredException("Refresh token has expired");
        }
        return refreshToken.getUserAccount();
    }

//    private SecretKey getSecretKey() {
//        return new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//    }
}
