package com.example.security.oauth2;

import java.util.Map;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.exception.request.NotFoundException;
import com.example.exception.request.StaleUpdateException;
import com.example.security.UserPrincipal;
import com.example.service.user.UserService;
import com.example.service.user.model.User;
import com.example.service.user.model.UserRegisterRequest;

@Service
public class OAuth2OidcUserService extends OidcUserService {
    private final UserService userService;

    public OAuth2OidcUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Load user from oidc user request
     * Return information from database if user exists
     * Insert info from oidc user and return if user not exists
     *
     * @param userRequest request to load oidc user from
     * @return custom user with info from database
     * @throws OAuth2AuthenticationException exception from loadUser()
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");
        User user;
        try {
            user = userService.getUserByEmail(email);
        } catch (NotFoundException notFoundException) {
            try {
                user = userService.createUser(UserRegisterRequest.builder()
                        .email(email)
                        .username((String) attributes.get("name"))
                        .imageUrl((String) attributes.get("picture"))
                        .lastname((String) attributes.get("family_name"))
                        .firstname((String) attributes.get("given_name"))
                        .build());
            } catch (StaleUpdateException | NotFoundException ex) {
                throw new RuntimeException("Insert User failed");
            }
        }
        return new UserPrincipal(user);
    }
}
