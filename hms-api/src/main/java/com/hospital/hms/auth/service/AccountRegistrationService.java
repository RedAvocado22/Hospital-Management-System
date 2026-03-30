package com.hospital.hms.auth.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.auth.request.AccountRegistrationRequest;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.IdentityProviderException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.keycloak.request.KeyCloakRequest;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRegistrationService extends BaseService<AccountRegistrationRequest, AccountInfo> {

    private final AccountRepository accountRepository;
    private final KeycloakService keycloakService;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public AccountInfo execute(AccountRegistrationRequest request) {
        return super.execute(request);
    }

    @Override
    protected AccountInfo doProcess(AccountRegistrationRequest request) {
        log.debug("User saved to local database: {}", request.getUsername());

        Role role = roleRepository.findByNameIgnoreCase(request.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found: " + request.getRole()));


        String keycloakUserId;
        try {
            KeyCloakRequest keyCloakRequest = KeyCloakRequest.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(request.getPassword())
                    .build();

            keycloakUserId = keycloakService.createUser(keyCloakRequest);
            keycloakService.assignRoleToUser(keycloakUserId, role.getName().toUpperCase());
        } catch (Exception e) {
            log.error("Keycloak error during account creation", e);
            throw new IdentityProviderException("Failed to register account in Keycloak");
        }

        Account account = null;
        try {
            account = Account.builder()
                    .id(UUID.fromString(keycloakUserId))
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .isActive(true)
                    .role(role)
                    .build();

            accountRepository.save(account);
        } catch (Exception e) {
            log.error("Database error during account creation - rolling back", e);
            keycloakService.deleteUser(request.getUsername());
            throw new BusinessException("Account registration failed — database error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return AccountInfo.from(account);
    }

    @Override
    protected void validate(AccountRegistrationRequest request) {
        super.validate(request);

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists in system");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists in system");
        }
    }
}
