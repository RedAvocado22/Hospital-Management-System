package com.hospital.hms.department.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.department.request.CreateDepartmentRequest;
import com.hospital.hms.department.request.SearchDepartmentRequest;
import com.hospital.hms.department.response.DepartmentResponse;
import com.hospital.hms.department.service.CreateDepartmentService;
import com.hospital.hms.department.service.GetDepartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<PaginatedResponse<DepartmentResponse>> getDepartments(
            @ModelAttribute SearchDepartmentRequest request
    ) {
        PaginatedResponse<DepartmentResponse> response = getDepartmentService.execute(request);
        return ApiResponse.success(response, "Get departments successfully", HttpStatus.OK.value());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request
    ) {
        createDepartmentService.execute(request);
        return ApiResponse.success(null, "Department created successfully", HttpStatus.CREATED.value());
    }
}
