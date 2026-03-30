package com.hospital.hms.auth.controller;

import com.hospital.hms.auth.request.SignInRequest;
import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.auth.service.SignInService;
import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.patient.request.CreatePatientRequest;
import com.hospital.hms.patient.response.PatientResponse;
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
    @Operation(
            summary = "Register a new patient",
            description = """
                    Registers a new patient account. This is the only public registration endpoint — \
                    employees are created by an Admin via POST /employees.
                    
                    **What happens internally:**
                    1. Validates username and email uniqueness
                    2. Saves Account + PatientInfo to MySQL
                    3. Provisions the user in Keycloak and assigns the PATIENT role
                    
                    If Keycloak provisioning fails, the MySQL record is rolled back (two-phase compensation). \
                    The caller always gets a consistent result.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Patient registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or duplicate username/email"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "Keycloak unavailable — registration rolled back"
            )
    })
    public ResponseEntity<ApiResponse<PatientResponse>> signUp(@Valid @RequestBody CreatePatientRequest request) {
        log.info("Received sign up request: {}", request.toLogString());

        PatientResponse data = createPatientService.execute(request);

        log.info("Successfully processed sign up request for: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "User registered successfully", HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/signin")
    @Operation(
            summary = "Sign in",
            description = """
                    Authenticates a user against Keycloak and returns a JWT access token and refresh token.
                    
                    **Token usage:** Include the access token in the Authorization header as `Bearer <token>` \
                    for all protected endpoints.
                    
                    **Token lifetime:** Access token expires per Keycloak realm config (default 5 minutes). \
                    Use the refresh token to obtain a new access token without re-login.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful — returns accessToken, refreshToken, and expiry info",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "Keycloak unavailable"
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
