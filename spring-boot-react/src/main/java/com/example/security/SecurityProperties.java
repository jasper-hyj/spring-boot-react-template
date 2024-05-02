package com.example.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Security prop
 */
@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {
    List<String> privateApis;
    List<String> roles;
}