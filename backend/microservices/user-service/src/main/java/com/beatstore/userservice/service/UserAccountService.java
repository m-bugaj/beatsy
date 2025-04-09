package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.RegisterRequestDTO;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userAccountRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String userHash = UUID.randomUUID().toString();

        UserAccount userAccount = new UserAccount(
                userHash,
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail(),
                passwordEncoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getFirstName(),
                registerRequestDTO.getLastName()
        );

        return userAccountRepository.save(userAccount);
    }
}
