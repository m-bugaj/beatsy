package com.beatstore.userservice.security;

import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserAccount userAccount = userAccountRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//
//        return new CustomUserDetails(userAccount);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to load user by username: {}", username);  // Logowanie przed wyszukaniem użytkownika
        UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        log.info("User found: {}", userAccount.getUsername());  // Logowanie po znalezieniu użytkownika
        return new CustomUserDetails(userAccount);
    }
}
