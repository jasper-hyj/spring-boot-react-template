package com.example.security.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth2 registration prop
 */
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
@Data
public class OAuth2GoogleRegistrationProperties {
    String clientId;
    String clientSecret;
    String scope;
    String clientName;
}