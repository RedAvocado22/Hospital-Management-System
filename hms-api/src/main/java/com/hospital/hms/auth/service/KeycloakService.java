package com.hospital.hms.auth.service;

import com.hospital.hms.auth.request.SignUpRequest;
import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.exception.IdentityProviderException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final WebClient webClient = WebClient.builder().build();
    private final Keycloak keycloak;

    @Value("${app.keycloak.server-url}")
    private String serverUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.client-id}")
    private String clientId;

    @Value("${app.keycloak.client-secret}")
    private String clientSecret;

    /**
     * Authenticates a user with Keycloak and returns access/refresh tokens.
     */
    public AuthResponse authenticate(String username, String password) {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            formData.add("client_secret", clientSecret);
        }
        formData.add("username", username);
        formData.add("password", password);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                return AuthResponse.builder()
                        .accessToken((String) response.get("access_token"))
                        .refreshToken((String) response.get("refresh_token"))
                        .expiresIn(((Number) response.get("expires_in")).longValue())
                        .refreshExpiresIn(((Number) response.get("refresh_expires_in")).longValue())
                        .build();
            }
        } catch (WebClientResponseException e) {
            log.error("Keycloak authentication failed: {}", e.getResponseBodyAsString());
            throw new IdentityProviderException("Keycloak Authentication Error: " + e.getResponseBodyAsString(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Unexpected error during Keycloak authentication", e);
            throw new IdentityProviderException("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        throw new IdentityProviderException("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Registers a new user in Keycloak.
     */
    public void createUser(SignUpRequest request) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getUsername());
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(request.getPassword());
        credentialRepresentation.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UsersResource usersResource = keycloak.realm(realm).users();
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 409) {
                log.warn("User already exists in Keycloak: {}", request.getUsername());
                throw new IdentityProviderException("User already exists in Keycloak", HttpStatus.CONFLICT);
            }

            if (response.getStatus() != 201) {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
                throw new IdentityProviderException("Failed to register user in Keycloak", HttpStatus.BAD_GATEWAY);
            }
        } catch (IdentityProviderException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error communicating with Keycloak", e);
            throw new IdentityProviderException("Error communicating with Keycloak", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
