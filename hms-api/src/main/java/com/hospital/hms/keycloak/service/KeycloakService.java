package com.hospital.hms.keycloak.service;

import com.hospital.hms.auth.dto.AuthTokenInfo;
import com.hospital.hms.exception.IdentityProviderException;
import com.hospital.hms.keycloak.request.KeyCloakRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
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
import java.util.List;
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
    public AuthTokenInfo authenticate(String username, String password) {
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
                return new AuthTokenInfo(
                        (String) response.get("access_token"),
                        (String) response.get("refresh_token"),
                        ((Number) response.get("expires_in")).longValue(),
                        ((Number) response.get("refresh_expires_in")).longValue()
                );
            }
        } catch (WebClientResponseException e) {
            log.error("Keycloak authentication failed: {}", e.getResponseBodyAsString());
            throw new IdentityProviderException(
                    "Keycloak Authentication Error: " + e.getResponseBodyAsString(),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            log.error("Unexpected error during Keycloak authentication", e);
            throw new IdentityProviderException("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        throw new IdentityProviderException("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Registers a new user in Keycloak.
     */
    public String createUser(KeyCloakRequest request) {
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
        try {
            Response response = usersResource.create(userRepresentation);

            if (response.getStatus() == 409) {
                log.warn("User already exists in Keycloak: {}", request.getUsername());
                throw new IdentityProviderException("User already exists in Keycloak", HttpStatus.CONFLICT);
            }

            if (response.getStatus() != 201) {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
                throw new IdentityProviderException("Failed to register user in Keycloak", HttpStatus.BAD_GATEWAY);
            }

            // Extract User ID from the response Location header
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Successfully created user {} with ID {}", request.getUsername(), userId);
            return userId;
        } catch (IdentityProviderException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error communicating with Keycloak", e);
            throw new IdentityProviderException("Error communicating with Keycloak", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Assigns a realm-level role to a user.
     */
    public void assignRoleToUser(String userId, String roleName) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
            log.info("Assigned role {} to user {}", roleName, user.getUsername());
        } catch (Exception e) {
            log.error("Failed to assign role {} to user ID {}", roleName, userId, e);
            throw new IdentityProviderException(
                    "Failed to assign permissions in Keycloak", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void updateRole(String userId, String newRoleName, String oldRoleName) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            RoleRepresentation newRole = keycloak.realm(realm).roles().get(newRoleName).toRepresentation();
            RoleRepresentation oldRole = keycloak.realm(realm).roles().get(oldRoleName).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(Collections.singletonList(oldRole));
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(newRole));
            log.info("Update role {} to user {}", newRoleName, user.getUsername());
        } catch (Exception e) {
            log.error("Failed to update role {} to user ID {}", newRoleName, userId, e);
            throw new IdentityProviderException(
                    "Failed to assign permissions in Keycloak", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Checks if a user already exists in Keycloak.
     */
    public boolean existsByUsername(String username) {
        return !keycloak.realm(realm).users().search(username).isEmpty();
    }

    public void deleteUser(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();

        List<UserRepresentation> usersRepresentation = usersResource.search(username);

        if (!usersRepresentation.isEmpty()) {
            usersResource.delete(usersRepresentation.get(0).getId());
        } else {
            log.warn("Nothing to delete in Keycloak");
        }
    }

    public void setUserEnabled(String userId, boolean enabled) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            user.setEnabled(enabled);
            keycloak.realm(realm).users().get(userId).update(user);
            log.info("User {}'s status is: {}", user.getUsername(), enabled);
        } catch (Exception e) {
            log.error("Failed to change user status");
            throw new IdentityProviderException(
                    "Failed to assign permissions in Keycloak", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void updateUserInfo(String accountId, String email, String firstName, String lastName) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(accountId).toRepresentation();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            keycloak.realm(realm).users().get(accountId).update(user);
            log.info("Successfully updated user info in Keycloak for user ID: {}", accountId);
        } catch (Exception e) {
            log.error("Failed to update user info in Keycloak for user ID: {}", accountId, e);
            throw new IdentityProviderException(
                    "Failed to update user info in Keycloak", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void updatePassword(String accountId, String newPassword) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            keycloak.realm(realm).users().get(accountId).resetPassword(credential);
            log.info("Successfully updated password for user ID: {}", accountId);
        } catch (Exception e) {
            log.error("Failed to update password in Keycloak for user ID: {}", accountId, e);
            throw new IdentityProviderException(
                    "Failed to change password", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
