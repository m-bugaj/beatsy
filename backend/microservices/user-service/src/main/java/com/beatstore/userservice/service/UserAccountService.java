package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.RegisterRequestDTO;
import com.beatstore.userservice.enums.UserRole;
import com.beatstore.userservice.exception.RoleNotFoundException;
import com.beatstore.userservice.model.Role;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.RoleRepository;
import com.beatstore.userservice.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;

    public UserAccountService(UserAccountRepository userAccountRepository, RoleRepository roleRepository) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
    }

    public UserAccount registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userAccountRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String userHash = UUID.randomUUID().toString();

        Optional<Role> defaultRole = roleRepository.findByName(UserRole.USER);
        if (defaultRole.isEmpty()) {
            throw new RoleNotFoundException("Role " + UserRole.USER + " not found!");
        }

        UserAccount userAccount = new UserAccount(
                userHash,
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail(),
                passwordEncoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getFirstName(),
                registerRequestDTO.getLastName(),
                defaultRole.get()
        );

        return userAccountRepository.save(userAccount);
    }
}
