package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.RegisterRequestDTO;
import com.beatstore.userservice.enums.UserRoleName;
import com.beatstore.userservice.exception.RoleNotFoundException;
import com.beatstore.userservice.model.Role;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.RoleRepository;
import com.beatstore.userservice.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
