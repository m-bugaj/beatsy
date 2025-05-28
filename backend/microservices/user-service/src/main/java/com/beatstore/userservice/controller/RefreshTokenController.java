package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.RefreshTokenRequest;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.service.TokenService;
import com.beatstore.userservice.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    private final TokenService tokenService;
    private final UserSessionService userSessionService;

    public RefreshTokenController(TokenService tokenService, UserSessionService userSessionService) {
        this.tokenService = tokenService;
        this.userSessionService = userSessionService;
    }

    @PostMapping("/auth/refresh-token")
    public AuthResponse refreshToken(
            HttpServletRequest request,
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        UserAccount userAccount = tokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
//        UserDTO user = UserDTO.builder()
//                .email(userAccount.getEmail())
//                .firstName(userAccount.getFirstName())
//                .lastName(userAccount.getLastName())
//                .build();
        String newJwtToken = tokenService.generateJwtToken(userAccount);
        String newRefreshToken = tokenService.generateRefreshToken(userAccount);
        userSessionService.validateAndRefreshSessionOrThrow(userAccount.getUserHash(), request);

        return new AuthResponse(newJwtToken, newRefreshToken);
    }
}
