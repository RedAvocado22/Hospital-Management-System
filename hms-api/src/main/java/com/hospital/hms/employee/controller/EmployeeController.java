package com.hospital.hms.employee.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.employee.request.CreateEmployeeRequest;
import com.hospital.hms.employee.request.EmployeeIdRequest;
import com.hospital.hms.employee.request.SearchEmployeeRequest;
import com.hospital.hms.employee.request.UpdateEmployeeRequest;
import com.hospital.hms.employee.response.EmployeeDetailResponse;
import com.hospital.hms.employee.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Endpoints for managing hospital staff")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final CreateEmployeeService createEmployeeService;
    private final GetEmployeeService getEmployeeService;
    private final GetEmployeeDetailService getEmployeeDetailService;
    private final UpdateEmployeeService updateEmployeeService;
    private final ActiveEmployeeService activeEmployeeService;
    private final DeActiveEmployeeService deActiveEmployeeService;

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
                    description = "Access denied — Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Department or Role not found"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        log.info("Creating employee: {}", request.getUsername());

        createEmployeeService.execute(request);

        log.info("Employee created: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null, "Employee created successfully", HttpStatus.CREATED.value())
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Search employees",
            description = "Returns a paginated list of employees. Supports filtering by name, email, department, gender, hire date, and more."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — Admin role required"
            )
    })
    public ResponseEntity<ApiResponse<PaginatedResponse<EmployeeDetailResponse>>> getEmployees(
            @Valid @ModelAttribute SearchEmployeeRequest request
    ) {
        PaginatedResponse<EmployeeDetailResponse> data = getEmployeeService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get employees successfully", HttpStatus.OK.value())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get employee by ID",
            description = "Returns detailed information about a single employee, including their account, department, and role."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employee retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    public ResponseEntity<ApiResponse<EmployeeDetailResponse>> getEmployee(
            @Parameter(description = "UUID of the employee to retrieve", required = true)
            @PathVariable UUID id
    ) {
        EmployeeIdRequest request = new EmployeeIdRequest(id);

        EmployeeDetailResponse data = getEmployeeDetailService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get employee successfully", HttpStatus.OK.value())
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update an employee",
            description = "Updates employee profile information including personal details, role, department, and hire date. Only accessible by Admins."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee, Department, or Role not found"
            )
    })
    public ResponseEntity<ApiResponse<EmployeeDetailResponse>> updateEmployee(
            @Parameter(description = "UUID of the employee to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployeeRequest request
    ) {
        request.setId(id);

        log.info("Updating employee id: {}", id);

        EmployeeDetailResponse data = updateEmployeeService.execute(request);

        log.info("Employee {} updated", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Update employee successfully", HttpStatus.OK.value())
        );
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Deactivate an employee",
            description = "Disables the employee's account. The employee will no longer be able to log in. Only accessible by Admins."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employee deactivated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>> deactivateEmployee(
            @Parameter(description = "UUID of the employee to deactivate", required = true)
            @PathVariable UUID id
    ) {
        log.info("Deactivating employee id: {}", id);

        EmployeeIdRequest request = new EmployeeIdRequest(id);
        deActiveEmployeeService.execute(request);

        log.info("Employee {} deactivated", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null, "Employee deactivated successfully", HttpStatus.OK.value())
        );
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Activate an employee",
            description = "Re-enables a previously deactivated employee account. Only accessible by Admins."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employee activated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — Admin role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>> activateEmployee(
            @Parameter(description = "UUID of the employee to activate", required = true)
            @PathVariable UUID id
    ) {
        log.info("Activating employee id: {}", id);

        EmployeeIdRequest request = new EmployeeIdRequest(id);
        activeEmployeeService.execute(request);

        log.info("Employee {} activated", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null, "Employee activated successfully", HttpStatus.OK.value())
        );
    }
}
