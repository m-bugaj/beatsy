package com.beatstore.userservice.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${wt.secret-key}")
    private String secret;

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String getSecret() {
        return secret;
    }
}
