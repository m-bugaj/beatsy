package com.beatstore.apigatewayweb.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;

@Component
public class JwtVerifier {

    private final RSAPublicKey publicKey;

    public JwtVerifier(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
