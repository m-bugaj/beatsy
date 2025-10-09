package com.beatstore.authservice.security;

import com.beatstore.authservice.dto.auth.AuthResponse;
import com.beatstore.authservice.dto.auth.LoginRequestDTO;
import com.beatstore.authservice.dto.auth.RegisterRequestDTO;
import com.beatstore.authservice.dto.auth.UserDTO;
import com.beatstore.authservice.model.UserAccount;
import com.beatstore.authservice.repository.UserAccountRepository;
import com.beatstore.authservice.service.UserAccountService;
import com.beatstore.authservice.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserAccountService userAccountService;
    private final UserSessionService userSessionService;

    public AuthService(AuthenticationManager authenticationManager, UserAccountRepository userAccountRepository, JwtService jwtService, CustomUserDetailsService customUserDetailsService, RefreshTokenService refreshTokenService, UserAccountService userAccountService, UserSessionService userSessionService) {
        this.authenticationManager = authenticationManager;
        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.userAccountService = userAccountService;
        this.userSessionService = userSessionService;
    }

    public AuthResponse login(HttpServletRequest request, LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getIdentifier(),
                        loginRequestDTO.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        UserDTO userDTO = new UserDTO(userDetails);
        String jwtToken = jwtService.generateToken(userDetails.getUserAccount());
        String refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUserAccount());
        ResponseCookie cookie = createJwtCookie(jwtToken);
        userSessionService.createSession(userDetails.getUserAccount(), request);
        log.info("Logged in successfully: {}", userDetails.getUsername());
        return new AuthResponse(jwtToken, refreshToken, cookie);
    }

    public UserAccount registerOrUpdateOAuth2User(Map<String, Object> attributes) {
        String subject = (String) attributes.get("sub"); // Sprawdzać po tym i po mailu czy użytkownik jest już w bazie
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String locale = (String) attributes.get("locale");

        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, firstName, lastName))
                .orElseGet(() -> createNewUser(subject, email, firstName, lastName));

        return userAccount;
//        return toDTO(userAccount);
    }

    public AuthResponse oAuth2Success(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();

        UserAccount userAccount = registerOrUpdateOAuth2User(attributes);

        String jwtToken = jwtService.generateToken(userAccount);
        String refreshToken = refreshTokenService.generateRefreshToken(userAccount);
        ResponseCookie cookie = createJwtCookie(jwtToken);

        return new AuthResponse(jwtToken, refreshToken, cookie);
    }

    // TODO: max age ciasteczka przenieść do konfiguracji
    private ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
    }

    private UserAccount updateExistingUser(UserAccount user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userAccountRepository.save(user);
    }

    private UserAccount createNewUser(String subject, String email, String firstName, String lastName) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(email, firstName, lastName);
        return userAccountService.registerUser(registerRequestDTO);
//        UserAccount newUser = UserAccount.builder()
//                .email(email)
//                .firstName(firstName)
//                .lastName(lastName)
//                .build();
//        return userAccountRepository.save(newUser);
    }

    private UserDTO toDTO(UserAccount user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
