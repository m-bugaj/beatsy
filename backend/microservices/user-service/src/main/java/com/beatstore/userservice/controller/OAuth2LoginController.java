package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.security.CustomUserDetails;
import com.beatstore.userservice.service.AuthService;
import com.beatstore.userservice.service.TokenService;
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
    public AuthResponse success(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();

        UserDTO customUserDetails = authService.registerOrUpdateOAuth2User(attributes);

        String jwtToken = tokenService.generateJwtToken(customUserDetails);
        String refreshToken = tokenService.generateRefreshToken(customUserDetails);

        return new AuthResponse(jwtToken, refreshToken);
    }
}
