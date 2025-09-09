package com.beatstore.apigatewayweb.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.beatstore.apigatewayweb.security.JwtVerifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtVerifier jwtVerifier;

    public JwtAuthenticationFilter(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String jwtToken = Optional.ofNullable(request.getCookies().getFirst("jwt"))
                .map(HttpCookie::getValue)
                .orElse(null);

        if (Objects.isNull(jwtToken)) {
            return chain.filter(exchange);
        }

        try {
            DecodedJWT decodedJWT = jwtVerifier.verifyToken(jwtToken);

            String userHash = decodedJWT.getClaim("userHash").asString();

            request.mutate()
                    .header("X-User-Hash", userHash)
                    .build();

            return chain.filter(exchange.mutate().request(request).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
