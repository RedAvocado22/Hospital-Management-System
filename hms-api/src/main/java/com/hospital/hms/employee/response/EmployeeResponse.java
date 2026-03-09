package com.hospital.hms.employee.response;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.department.response.DepartmentResponse;
import com.hospital.hms.employee.entity.EmployeeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Employee details including account and department information")
public record EmployeeResponse(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Login username", example = "dr.nguyen")
        String username,

        @Schema(description = "Email address", example = "nguyen@hospital.com")
        String email,

        @Schema(description = "First name", example = "Minh")
        String firstName,

        @Schema(description = "Last name", example = "Nguyen")
        String lastName,

        @Schema(description = "Full display name", example = "Minh Nguyen")
        String fullName,

        @Schema(description = "Date of birth", example = "1990-05-15")
        LocalDate dob,

        @Schema(description = "Gender", example = "MALE")
        Gender gender,

        @Schema(description = "Residential address", example = "123 Le Loi, District 1, HCMC")
        String address,

        @Schema(description = "Phone number", example = "0901234567")
        String phone,

        @Schema(description = "Whether the account is active", example = "true")
        boolean isActive,

        @Schema(description = "Assigned role name", example = "DOCTOR")
        String role,

        @Schema(description = "Assigned department")
        DepartmentResponse department,

        @Schema(description = "Employment start date", example = "2026-01-15")
        LocalDate hireDate,

        @Schema(description = "Unique employee code", example = "EMP-001")
        String code,

        @Schema(description = "Record creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last modification timestamp")
        LocalDateTime updatedAt
) {
    public static EmployeeResponse from(EmployeeInfo employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getAccount().getUsername(),
                employee.getAccount().getEmail(),
                employee.getAccount().getFirstName(),
                employee.getAccount().getLastName(),
                employee.getAccount().getFullName(),
                employee.getAccount().getDob(),
                employee.getAccount().getGender(),
                employee.getAccount().getAddress(),
                employee.getAccount().getPhone(),
                employee.getAccount().getIsActive(),
                employee.getAccount().getRole().getName(),
                DepartmentResponse.from(employee.getDepartment()),
                employee.getHireDate(),
                employee.getCode(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
