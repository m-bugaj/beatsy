package com.beatstore.authservice.service;

import com.beatstore.authservice.dto.auth.RegisterRequestDTO;
import com.beatstore.authservice.enums.UserRoleName;
import com.beatstore.authservice.exception.RoleNotFoundException;
import com.beatstore.authservice.model.Role;
import com.beatstore.authservice.model.UserAccount;
import com.beatstore.authservice.repository.RoleRepository;
import com.beatstore.authservice.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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

        Set<Role> defaultRoles = roleRepository.findAllByNameIn(UserRoleName.getDefaultRoles());
        if (Objects.isNull(defaultRoles) || defaultRoles.isEmpty()) {
            throw new RoleNotFoundException("Roles " + UserRoleName.getDefaultRoles() + " not found!");
        }

        UserAccount userAccount = new UserAccount(
                userHash,
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail(),
                passwordEncoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getFirstName(),
                registerRequestDTO.getLastName(),
                defaultRoles
        );

        return userAccountRepository.save(userAccount);
    }
}
