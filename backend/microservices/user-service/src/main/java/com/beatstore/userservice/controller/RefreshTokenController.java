package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.RefreshTokenRequest;
import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.model.UserAccount;
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
        UserAccount userAccount = tokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
//        UserDTO user = UserDTO.builder()
//                .email(userAccount.getEmail())
//                .firstName(userAccount.getFirstName())
//                .lastName(userAccount.getLastName())
//                .build();
        String newJwtToken = tokenService.generateJwtToken(userAccount);
        String newRefreshToken = tokenService.generateRefreshToken(userAccount);

        return new AuthResponse(newJwtToken, newRefreshToken);
    }
}
