package com.beatstore.userservice.controller;

import com.beatstore.userservice.dto.auth.RegisterRequestDTO;
import com.beatstore.userservice.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountService userAccountService;

    public AuthController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        userAccountService.registerUser(registerRequestDTO);
        return ResponseEntity.ok().build();
    }
}
