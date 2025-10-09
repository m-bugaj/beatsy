package com.beatstore.authservice.security;

import com.beatstore.authservice.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Slf4j
public class CustomUserDetails implements UserDetails {
    private final UserAccount userAccount;

    public CustomUserDetails(UserAccount userAccount) {
        this.userAccount = userAccount;
        log.info("CustomUserDetails created for user: {}", userAccount.getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userAccount.getPasswordHash();
    }

    @Override
    public String getUsername() {
//        String username = userAccount.getUsername();
        String identifier = (userAccount.getUsername() != null) ? userAccount.getUsername() : userAccount.getEmail();
        log.info("Returning identifier: {}", identifier);
        return identifier;
    }

    public String getEmail() {
        return userAccount.getEmail();
    }

    public String getFirstName() {
        return userAccount.getFirstName();
    }

    public String getLastName() {
        return userAccount.getLastName();
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }
}
