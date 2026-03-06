package com.hospital.hms.auth.controller;

import com.hospital.hms.auth.request.SignInRequest;
import com.hospital.hms.auth.response.AccountResponse;
import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.auth.service.SignInService;
import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.patient.request.CreatePatientRequest;
import com.hospital.hms.patient.service.CreatePatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final CreatePatientService createPatientService;
    private final SignInService signInService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account in Keycloak and local database")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or duplicate resource"
            )
    })
    public ResponseEntity<ApiResponse<AccountResponse>> signUp(@Valid @RequestBody CreatePatientRequest request) {
        log.info("Received sign up request: {}", request.toLogString());

        AccountResponse data = createPatientService.execute(request);

        log.info("Successfully processed sign up request for: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "User registered successfully", HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/signin")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns Keycloak access and refresh tokens")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        log.info("Received sign in request: {}", request.toLogString());

        AuthResponse response = signInService.signInUser(request);

        log.info("Successfully processed sign in request for: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(response, "Login successful", HttpStatus.OK.value())
        );
    }
}
