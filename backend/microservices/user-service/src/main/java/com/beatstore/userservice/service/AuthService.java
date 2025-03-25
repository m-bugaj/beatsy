package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.LoginRequestDTO;
import com.beatstore.userservice.repository.UserAccountRepository;
import com.beatstore.userservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserAccountRepository userAccountRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        log.info("Logged in successfully: {}", userDetails.getUsername());
        return token;
    }
}
