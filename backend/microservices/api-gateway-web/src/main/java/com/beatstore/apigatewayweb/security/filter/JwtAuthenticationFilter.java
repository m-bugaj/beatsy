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

import java.util.List;
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

        String path = exchange.getRequest().getURI().getPath();
        boolean securedEndpoint = path.contains("/secured/");

        String jwtToken = Optional.ofNullable(request.getCookies().getFirst("jwt"))
                .map(HttpCookie::getValue)
                .orElse(null);

        if (securedEndpoint && jwtToken == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (Objects.isNull(jwtToken)) {
            return chain.filter(exchange);
        }

        try {
            DecodedJWT decodedJWT = jwtVerifier.verifyToken(jwtToken);

            String userHash = decodedJWT.getClaim("userHash").asString();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Hash", userHash)
                    .header("X-Roles", String.join(",", roles))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
