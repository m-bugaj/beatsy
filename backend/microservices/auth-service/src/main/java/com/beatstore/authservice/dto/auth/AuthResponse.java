package com.beatstore.authservice.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Getter
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private ResponseCookie cookie;

    public AuthResponse(String accessToken, String refreshToken, ResponseCookie cookie) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.cookie = cookie;
    }
}
