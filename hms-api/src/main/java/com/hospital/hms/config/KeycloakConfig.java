package com.hospital.hms.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.server-url}")
    private String serverUrl;

    @Value("${app.keycloak.admin.username}")
    private String adminUsername;

    @Value("${app.keycloak.admin.password}")
    private String adminPassword;

    @Value("${app.keycloak.admin.client-id}")
    private String adminClientId;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // Admin API operations usually need to authenticate against the master realm
                .grantType(OAuth2Constants.PASSWORD)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(adminClientId)
                .build();
    }
}
