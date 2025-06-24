package com.beatstore.userservice.context;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beatstore.userservice.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
public class JwtRequestProcessingFilter extends OncePerRequestFilter {

    private final RequestContext requestContext;
    private final JwtService jwtService;

    @Autowired
    public JwtRequestProcessingFilter(RequestContext requestContext, JwtService jwtService) {
        this.requestContext = requestContext;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (Objects.nonNull(jwtToken)) {
            try {
                DecodedJWT jwt = jwtService.verifyToken(jwtToken);

                requestContext.setUsername(jwt.getSubject());
                requestContext.setUserHash(jwt.getClaim("userHash").asString());
//                requestContext.setRole(jwt.getClaim("role").asString());
//                requestContext.setSubscriptionHash(jwt.getClaim("subscriptionHash").asString());

                // TODO: Tutaj w przyszłości należy zadbać o role użytkownika (trzeci argument)
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                jwt.getSubject(), null, Collections.emptyList())
                );

            } catch (JWTVerificationException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
