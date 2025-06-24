package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.RefreshTokenRequest;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.security.JwtService;
import com.beatstore.userservice.security.RefreshTokenService;
import com.beatstore.userservice.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserSessionService userSessionService;

    public RefreshTokenController(RefreshTokenService refreshTokenService, JwtService jwtService, UserSessionService userSessionService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userSessionService = userSessionService;
    }

//    @PostMapping("/auth/refresh-token")
//    public AuthResponse refreshToken(
//            HttpServletRequest request,
//            @RequestBody RefreshTokenRequest refreshTokenRequest
//    ) {
//        UserAccount userAccount = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
////        UserDTO user = UserDTO.builder()
////                .email(userAccount.getEmail())
////                .firstName(userAccount.getFirstName())
////                .lastName(userAccount.getLastName())
////                .build();
//        String newJwtToken = jwtService.generateToken(userAccount);
//        String newRefreshToken = refreshTokenService.generateRefreshToken(userAccount);
//        userSessionService.validateAndRefreshSessionOrThrow(userAccount.getUserHash(), request);
//
//        return new AuthResponse(newJwtToken, newRefreshToken);
//    }
}
