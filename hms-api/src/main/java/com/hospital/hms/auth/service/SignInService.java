package com.hospital.hms.auth.service;

import com.hospital.hms.auth.dto.AuthTokenInfo;
import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.auth.request.SignInRequest;
import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {

    private final KeycloakService keycloakService;
    private final AccountRepository accountRepository;

    public AuthResponse signInUser(SignInRequest request) {
        log.info("Processing SignInService.signInUser: {}", request.toLogString());
        request.initialize();

        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new BusinessException("Invalid username or password")
        );

        if (Boolean.FALSE.equals(account.getIsActive())) {
            throw new BusinessException("The account is de-activated. Please contact administrator.");
        }

        AuthTokenInfo tokenInfo = keycloakService.authenticate(request.getUsername(), request.getPassword());

        return AuthResponse.builder()
                .accessToken(tokenInfo.accessToken())
                .refreshToken(tokenInfo.refreshToken())
                .expiresIn(tokenInfo.expiresIn())
                .refreshExpiresIn(tokenInfo.refreshExpiresIn())
                .build();
    }
}

