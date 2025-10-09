package com.beatstore.authservice.security;

import com.beatstore.authservice.model.UserAccount;
import com.beatstore.authservice.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
//        UserAccount userAccount = userAccountRepository.findByUsername(username)
//                .orElseThrow(() -> {
//                    log.warn("User not found: {}", username);
//                    return new UsernameNotFoundException("User not found");
//                });

        UserAccount userAccount = userAccountRepository.findByUsername(identifier)
                .or(() -> userAccountRepository.findByEmail(identifier)) // jeśli nie ma po username, szuka po emailu
                .orElseThrow(() -> {
                    log.warn("User not found: {}", identifier);
                    return new UsernameNotFoundException("User not found with username or email: " + identifier);
                });

        log.info("User found: {}", userAccount.getUsername());
        return new CustomUserDetails(userAccount);
    }
}
