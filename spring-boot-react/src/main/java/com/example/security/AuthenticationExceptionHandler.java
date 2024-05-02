package com.example.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.model.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AuthenticationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handle unauthorized(unauthenticated) exception (401)
     * Return json if URI starts with /auth/api/
     * Redirect to login page in other cases
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        if (isApiRequest(request)) {
            handleApiUnauthorizedRequest(response, request.getRequestURI());
        } else {
            response.sendRedirect("/login");
        }
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth/api/");
    }

    private void handleApiUnauthorizedRequest(HttpServletResponse response, String requestUri) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),
                ApiResponse.error(HttpStatus.UNAUTHORIZED, "Request unauthorized: " + requestUri));
    }
}
