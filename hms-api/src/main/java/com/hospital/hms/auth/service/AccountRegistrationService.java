package com.hospital.hms.auth.service;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.auth.request.AccountRegistrationRequest;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.IdentityProviderException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.keycloak.request.KeyCloakRequest;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRegistrationService extends BaseService<AccountRegistrationRequest, Account> {

    private final AccountRepository accountRepository;
    private final KeycloakService keycloakService;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    protected Account doProcess(AccountRegistrationRequest request) {
        log.debug("User saved to local database: {}", request.getUsername());

        Role role = roleRepository.findByNameIgnoreCase(request.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found: " + request.getRole()));

        Account account = Account.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .role(role)
                .build();

        accountRepository.save(account);

        try {
            //3. save to keycloak
            String keycloakUserId;
            KeyCloakRequest keyCloakRequest = KeyCloakRequest.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(request.getPassword())
                    .build();
            keycloakUserId = keycloakService.createUser(keyCloakRequest);

            // 4. Assign permissions in Keycloak
            keycloakService.assignRoleToUser(keycloakUserId, role.getName().toUpperCase());
        } catch (Exception e) {
            log.error("Keycloak error during account creation - rolling back", e);
            keycloakService.deleteUser(request.getUsername());
            throw new IdentityProviderException("Failed to register account in Keycloak — rolled back");
        }

        return account;
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
