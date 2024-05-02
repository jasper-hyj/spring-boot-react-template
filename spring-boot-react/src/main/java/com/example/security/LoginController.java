package com.example.security;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    /**
     * Handles the /login endpoint.
     * Redirects to /portal if the user is already logged in.
     *
     * @param principal The authenticated user principal.
     * @return Redirects to the React /login page if user is not logged in,
     *         otherwise redirects to portal page.
     * @throws IOException
     * @throws ServletException
     */
    @GetMapping("/login")
    public void loginPage(HttpServletRequest request, HttpServletResponse response,
            @AuthenticationPrincipal UserPrincipal principal) throws IOException, ServletException {
        if (principal != null) {
            response.sendRedirect("/portal");
        } else {
            request.getRequestDispatcher("/").forward(request, response);
        }
    }
}
