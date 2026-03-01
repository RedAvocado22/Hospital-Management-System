package com.hospital.hms.auth.service;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.auth.request.SignUpRequest;
import com.hospital.hms.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final KeycloakService keycloakService;

    @Transactional
    public void signUpUser(SignUpRequest request) {
        log.info("Attempting to sign up user with username: {}", request.getUsername());

        // 1. Validation and existence checks
        if (accountRepository.existsByUsername(request.getUsername())) {
            log.debug("Username {} already exists locally", request.getUsername());
            throw new DuplicateResourceException("Username already exists");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            log.debug("Email {} already exists locally", request.getEmail());
            throw new DuplicateResourceException("Email already exists");
        }

        Role defaultRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> {
                    log.error("Failed to find default role PATIENT in local database");
                    return new RuntimeException("Default role PATIENT not found");
                });

        // 2. Save user to MySQL first
        Account account = Account.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .role(defaultRole)
                .build();

        accountRepository.save(account);
        log.debug("User saved to local database: {}", request.getUsername());

        // 3. Register user with Keycloak
        // If this fails, @Transactional will rollback the local save.
        keycloakService.createUser(request);

        log.info("User registered successfully in both MySQL and Keycloak: {}", request.getUsername());
    }
}
