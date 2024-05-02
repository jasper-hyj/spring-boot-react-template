package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.security.csrf.CsrfCookieFilter;
import com.example.security.csrf.SpaCsrfTokenRequestHandler;
import com.example.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.security.oauth2.OAuth2OidcUserService;
import com.example.service.logging.RequestLoggingFilter;

// Security configuration for spring security
@Configuration
// Enable the spring security
@EnableWebSecurity
// Default method for spring security
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final OAuth2OidcUserService oAuth2OidcUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;
    private final CsrfCookieFilter csrfCookieFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    public SecurityConfig(SecurityProperties securityProperties, OAuth2OidcUserService oAuth2OidcUserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, CsrfCookieFilter csrfCookieFilter,
            SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler, RequestLoggingFilter requestLoggingFilter,
            AuthenticationExceptionHandler authenticationExceptionHandler) {
        this.securityProperties = securityProperties;
        this.oAuth2OidcUserService = oAuth2OidcUserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.spaCsrfTokenRequestHandler = spaCsrfTokenRequestHandler;
        this.csrfCookieFilter = csrfCookieFilter;
        this.requestLoggingFilter = requestLoggingFilter;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
                ROLE_ADMIN > ROLE_USER
                """);
        return hierarchy;
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    /**
     * Spring Security settings
     *
     * @param http httpsecurity object
     * @return filter chain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // Set required all request be https
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())

                // Set use oauth2 login
                .oauth2Login(oauth2 -> oauth2
                        // Default login page to /login
                        .loginPage("/login")
                        // Custom userinfo system
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oAuth2OidcUserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler))

                // Logout Handler
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll())

                // Public and private request set up
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(securityProperties.getPrivateApis().toArray(String[]::new)).authenticated()
                            .anyRequest().permitAll();
                })

                // Handle Access Denied Exception (403)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExceptionHandler)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/portal?error");
                        }))

                // enable csrf protect
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(spaCsrfTokenRequestHandler))
                .addFilterAfter(csrfCookieFilter, BasicAuthenticationFilter.class)

                // Filter for info logging
                .addFilterBefore(requestLoggingFilter, BasicAuthenticationFilter.class)

                .build();
    }

}