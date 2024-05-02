package com.example.security.csrf;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.*;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/*
 * Single page application (Spa) Csrf token request handler
 * Relates to the cookie set by the CsrfCookieFilter
 */
@Component
public class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.delegate.handle(request, response, csrfToken);
    }

    /**
     * Get csrf token value from cookie
     */
    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("XSRF-TOKEN")) {
                return cookie.getValue();
            }
        }
        throw new CsrfException("csrf token not found.");
    }
}
