package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.auth.AuthResponse;
import com.beatstore.userservice.dto.auth.LoginRequestDTO;
import com.beatstore.userservice.dto.auth.UserDTO;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.UserAccountRepository;
import com.beatstore.userservice.security.CustomUserDetails;
import com.beatstore.userservice.security.CustomUserDetailsService;
import com.beatstore.userservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, UserAccountRepository userAccountRepository, JwtService jwtService, CustomUserDetailsService customUserDetailsService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    public AuthResponse login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsernameOrEmail(),
                        loginRequestDTO.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDTO userDTO = new UserDTO(userDetails);
        String jwtToken = tokenService.generateJwtToken(userDTO);
        String refreshToken = tokenService.generateRefreshToken(userDTO);
        log.info("Logged in successfully: {}", userDetails.getUsername());
        return new AuthResponse(jwtToken, refreshToken);
    }

    public UserDTO registerOrUpdateOAuth2User(Map<String, Object> attributes) {
        String subject = (String) attributes.get("sub"); // Sprawdzać po tym i po mailu czy użytkownik jest już w bazie
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String locale = (String) attributes.get("locale");

        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, firstName, lastName))
                .orElseGet(() -> createNewUser(subject, email, firstName, lastName));

        return toDTO(userAccount);
    }

    private UserAccount updateExistingUser(UserAccount user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userAccountRepository.save(user);
    }

    private UserAccount createNewUser(String subject, String email, String firstName, String lastName) {
        UserAccount newUser = UserAccount.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        return userAccountRepository.save(newUser);
    }

    private UserDTO toDTO(UserAccount user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
