package com.hospital.hms.employee.response;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.department.dto.DepartmentInfo;
import com.hospital.hms.employee.entity.EmployeeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Login username", example = "dr.nguyen")
        String username,

        @Schema(description = "Email address", example = "nguyen@hospital.com")
        String email,

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
        DepartmentInfo department,

        @Schema(description = "Employment start date", example = "2026-01-15")
        LocalDate hireDate,

        @Schema(description = "Unique employee code", example = "EMP-001")
        String code
) {
    public static EmployeeResponse from(EmployeeInfo employee, AccountInfo account, DepartmentInfo department) {
        return new EmployeeResponse(
                employee.getId(),
                account.username(),
                account.email(),
                account.fullName(),
                account.dob(),
                account.gender(),
                account.address(),
                account.phone(),
                account.active(),
                account.role(),
                department,
                employee.getHireDate(),
                employee.getCode()
        );
    }
}
