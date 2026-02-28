package com.hospital.hms.auth.controller;

import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.auth.request.SignInRequest;
import com.hospital.hms.auth.request.SignUpRequest;
import com.hospital.hms.auth.service.SignInService;
import com.hospital.hms.auth.service.SignUpService;
import com.hospital.hms.base.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account in Keycloak and local database")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        request.initialize();
        log.info("Received sign up request: {}", request.toLogString());

        signUpService.signUpUser(request);

        log.info("Successfully processed sign up request for: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "User registered successfully", HttpStatus.CREATED.value()));
    }

    @PostMapping("/signin")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns Keycloak access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        request.initialize();
        log.info("Received sign in request: {}", request.toLogString());

        AuthResponse response = signInService.signInUser(request);

        log.info("Successfully processed sign in request for: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful", HttpStatus.OK.value()));
    }
}
