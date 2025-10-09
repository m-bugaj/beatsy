package com.beatstore.authservice.controller;

import com.beatstore.authservice.security.JwtService;
import com.beatstore.authservice.security.RefreshTokenService;
import com.beatstore.authservice.service.UserSessionService;
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
