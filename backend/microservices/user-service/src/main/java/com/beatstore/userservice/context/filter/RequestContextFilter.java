package com.beatstore.userservice.context.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.beatstore.userservice.context.RequestContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Order(2)
public class RequestContextFilter extends OncePerRequestFilter {

    private final RequestContext requestContext;

    public RequestContextFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof DecodedJWT jwt) {
            requestContext.setUsername(jwt.getSubject());
            requestContext.setUserHash(jwt.getClaim("userHash").asString());
//                requestContext.setRole(jwt.getClaim("role").asString());
//                requestContext.setSubscriptionHash(jwt.getClaim("subscriptionHash").asString());

        }

        filterChain.doFilter(request, response);
    }
}
