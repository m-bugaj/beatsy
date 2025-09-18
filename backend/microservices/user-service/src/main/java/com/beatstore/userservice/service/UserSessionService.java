package com.beatstore.userservice.service;

import com.beatstore.userservice.exception.UserSessionExpired;
import com.beatstore.userservice.exception.UserSessionNotFound;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.model.UserSession;
import com.beatstore.userservice.repository.RefreshTokenRepository;
import com.beatstore.userservice.repository.UserAccountRepository;
import com.beatstore.userservice.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserSessionService {
    @Value("${user-session.expiration-period}")
    private Integer sessionExpirationPeriod;

    private final UserSessionRepository userSessionRepository;
    private final UserAccountRepository userAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserSessionService(UserSessionRepository userSessionRepository, UserAccountRepository userAccountRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userAccountRepository = userAccountRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private void refreshSession(UserSession session, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = extractClientIp(request);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(sessionExpirationPeriod);
        session.setExpiresAt(expiresAt);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setLastActivity(now);
        userSessionRepository.save(session);
    }

    @Transactional
    public void createSession(UserAccount userAccount, HttpServletRequest request) {
        UserSession session = userSessionRepository.findByUserHash(userAccount.getUserHash())
                .orElse(new UserSession());
        session.setUserAccount(userAccount);
        session.setUserHash(userAccount.getUserHash());
        refreshSession(session, request);
    }

    public void validateAndRefreshSessionOrThrow(String userHash, HttpServletRequest request) {
        UserSession session = userSessionRepository.findByUserHash(userHash)
                .orElseThrow(() -> new UserSessionNotFound(userHash));
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserSessionExpired(userHash);
        }
        refreshSession(session, request);
    }

    private void clearSession(String userHash) {
        Optional<UserSession> oldSession = userSessionRepository.findByUserHash(userHash);
        oldSession.ifPresent(userSessionRepository::delete);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0]; // jeśli przez proxy
        }
        return request.getRemoteAddr(); // lokalnie
    }
}
