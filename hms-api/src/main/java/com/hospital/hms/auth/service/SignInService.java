package com.hospital.hms.auth.service;

import com.hospital.hms.auth.request.SignInRequest;
import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {

    private final KeycloakService keycloakService;

    public AuthResponse signInUser(SignInRequest request) {
        log.info("Processing SignInService.signInUser: {}", request.toLogString());
        request.initialize();
        return keycloakService.authenticate(request.getUsername(), request.getPassword());
    }
}
