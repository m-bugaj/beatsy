package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.security.AuthService;
import com.beatstore.userservice.security.RefreshTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;

public class OAuth2LoginController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public OAuth2LoginController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/auth/oauth2/success")
    public ResponseEntity<String> success(OAuth2AuthenticationToken authentication) {
        AuthResponse authResponse = authService.oAuth2Success(authentication);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authResponse.getCookie().toString())
                .body("Login successful");
    }
}
