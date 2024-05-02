package com.example.service.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security.UserPrincipal;
import com.example.service.user.model.User;

import java.io.IOException;

@Component
@Log4j2
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final String DEFAULT_LOG_MESSAGE_FORMAT = "{} -- {}:{}";
    private static final String AUTHENTICATED_LOG_MESSAGE_FORMAT = "{}:{} -- {}:{}";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURI().toString();
            if (shouldLogRequest(url)) {
                logRequest(request, url);
            }
        } catch (Exception ex) {
            log.error("Error occurred while logging request:    {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldLogRequest(String url) {
        return !url.endsWith("/") && !url.contains(".");
    }

    private void logRequest(HttpServletRequest request, String url) {
        String method = request.getMethod();
        String ip = LogUtil.getIp(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.user();
            log.debug(AUTHENTICATED_LOG_MESSAGE_FORMAT, ip, user.getEmail(), method, url);
        } else {
            log.debug(DEFAULT_LOG_MESSAGE_FORMAT, ip, method, url);
        }
    }
}
