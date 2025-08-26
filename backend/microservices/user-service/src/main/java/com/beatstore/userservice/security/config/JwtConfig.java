package com.beatstore.userservice.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

//    @Value("${wt.secret-key}")
//    private String secret;

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public JwtConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.RSA256(publicKey, privateKey);
    }

//    @Bean
//    public Algorithm jwtAlgorithm() {
//        return Algorithm.HMAC256(secret);
//    }

//    public String getSecret() {
//        return secret;
//    }
}
