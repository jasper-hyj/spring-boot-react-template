package com.example.security.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * All cors related properties
 */
@Component
@ConfigurationProperties(prefix = "spring.security.cors")
@Data
public class CorsProperties {
    boolean allowCredentials;
    List<String> allowedOrigins;
    List<String> allowedHeaders;
    List<String> exposedHeaders;
    List<String> allowedMethods;
}
