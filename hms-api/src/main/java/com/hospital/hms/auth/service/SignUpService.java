package com.hospital.hms.auth.service;

import com.hospital.hms.auth.request.SignUpRequest;
import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.IdentityProviderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.core.Response;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final Keycloak keycloak;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Transactional
    public void signUpUser(SignUpRequest request) {
        log.info("Attempting to sign up user with username: {}", request.getUsername());

        if (accountRepository.existsByUsername(request.getUsername())) {
            log.debug("Username {} already exists locally", request.getUsername());
            throw new DuplicateResourceException("Username already exists");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            log.debug("Email {} already exists locally", request.getEmail());
            throw new DuplicateResourceException("Email already exists");
        }

        log.debug("Local constraints passed for user {}", request.getUsername());

        Role defaultRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> {
                    log.error("Failed to find default role PATIENT in local database");
                    return new RuntimeException("Default role PATIENT not found");
                });

        log.debug("Fetched default role ID: {}", defaultRole.getId());

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
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != 201 && response.getStatus() != 409) {
            throw new IdentityProviderException(
                    "Failed to register user in Keycloak: Status " + response.getStatus(),
                    org.springframework.http.HttpStatus.BAD_GATEWAY
            );
        }

        if (response.getStatus() == 409) {
            throw new DuplicateResourceException("User already exists in Keycloak (Conflict)");
        }

        Account account = Account.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .role(defaultRole)
                .build();

        accountRepository.save(account);
        log.info("User registered successfully locally: {}", request.getUsername());
    }
}
