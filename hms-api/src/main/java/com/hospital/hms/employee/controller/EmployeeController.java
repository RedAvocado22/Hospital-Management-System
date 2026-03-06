package com.hospital.hms.employee.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.employee.request.CreateEmployeeRequest;
import com.hospital.hms.employee.service.CreateEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Endpoints for managing hospital staff")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final CreateEmployeeService createEmployeeService;

    @Operation(
            summary = "Register a new employee",
            description = "Creates a local account, links it to a department, and provisions the user in Keycloak. Only accessible by Admins."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Employee successfully created",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or duplicate resource (Username/Email/Code already exists)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Department or Role not found"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        createEmployeeService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null, "Employee created successfully", HttpStatus.CREATED.value())
        );
    }
}