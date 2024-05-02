package com.example.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.example.service.user.model.User;
import com.example.service.user.model.UserStatus;

/*
 * UserPrincipal for Authentication purpose
 * Contains custom User inside
 */
public record UserPrincipal(User user) implements UserDetails, OidcUser {

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // OidcUser methods
    @Override
    public Map<String, Object> getAttributes() {
        // Implement if necessary
        return null;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        // Implement if necessary
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        // Implement if necessary
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        // Implement if necessary
        return null;
    }
}