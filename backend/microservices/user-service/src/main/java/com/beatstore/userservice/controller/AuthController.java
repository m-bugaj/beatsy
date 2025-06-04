package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.LoginRequestDTO;
import com.beatstore.userservice.dto.auth.RegisterRequestDTO;
import com.beatstore.userservice.service.AuthService;
import com.beatstore.userservice.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountService userAccountService;
    private final AuthService authService;

    public AuthController(UserAccountService userAccountService, AuthService authService) {
        this.userAccountService = userAccountService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        userAccountService.registerUser(registerRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(
            HttpServletRequest request,
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        log.info("Received login request for username: {}", loginRequestDTO.getIdentifier());
        return ResponseEntity.ok(authService.login(request, loginRequestDTO));
    }
}
