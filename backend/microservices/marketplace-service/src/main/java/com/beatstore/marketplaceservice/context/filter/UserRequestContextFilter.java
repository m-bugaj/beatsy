package com.beatstore.marketplaceservice.context.filter;

import com.beatstore.marketplaceservice.context.RequestContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserRequestContextFilter extends OncePerRequestFilter {

    private final RequestContext requestContext;

    public UserRequestContextFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String userHash = request.getHeader("X-User-Hash");

        requestContext.setUserHash(userHash);

        filterChain.doFilter(request, response);
    }
}
