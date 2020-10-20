package com.example.resourceserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kofax.local", ignoreInvalidFields = true)
@Getter
@Setter
public class KeycloakProperties {
    private String keycloakUrl;
    private String adminRealm;
    private String username;
    private String password;
    private String adminCliendId;
    private String realm;
    private String outputDir;
}
