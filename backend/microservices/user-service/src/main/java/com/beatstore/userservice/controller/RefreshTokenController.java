package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.RefreshTokenRequest;
import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    private final TokenService tokenService;

    public RefreshTokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        UserDTO user = tokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        String newJwtToken = tokenService.generateJwtToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponse(newJwtToken, newRefreshToken);
    }
}
