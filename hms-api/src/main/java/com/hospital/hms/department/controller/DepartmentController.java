package com.hospital.hms.department.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.department.request.CreateDepartmentRequest;
import com.hospital.hms.department.request.SearchDepartmentRequest;
import com.hospital.hms.department.response.DepartmentResponse;
import com.hospital.hms.department.service.CreateDepartmentService;
import com.hospital.hms.department.service.GetDepartmentService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "Endpoints for managing hospital departments")
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

    private final GetDepartmentService getDepartmentService;
    private final CreateDepartmentService createDepartmentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Search departments",
            description = "Returns a paginated list of departments. Supports filtering by name, active status, and date ranges."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Departments retrieved successfully",
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
    public ResponseEntity<ApiResponse<PaginatedResponse<DepartmentResponse>>> getDepartments(
            @ModelAttribute SearchDepartmentRequest request
    ) {
        PaginatedResponse<DepartmentResponse> response = getDepartmentService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(response, "Get departments successfully", HttpStatus.OK.value())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new department",
            description = "Creates a new hospital department with a name and active status. Department names must be unique."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Department created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or duplicate department name"
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
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request
    ) {
        DepartmentResponse data = createDepartmentService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "Department created successfully", HttpStatus.CREATED.value())
        );
    }
}
