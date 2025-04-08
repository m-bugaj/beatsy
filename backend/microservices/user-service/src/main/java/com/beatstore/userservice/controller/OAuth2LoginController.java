package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.security.CustomUserDetails;
import com.beatstore.userservice.service.AuthService;
import com.beatstore.userservice.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public class OAuth2LoginController {

    private final AuthService authService;
    private final TokenService tokenService;

    public OAuth2LoginController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @GetMapping("/auth/oauth2/success")
    public ResponseEntity<AuthResponse> success(OAuth2AuthenticationToken authentication) {
        return ResponseEntity.ok(authService.oAuth2Success(authentication));
    }
}
