package com.hospital.hms.employee.response;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.department.response.DepartmentResponse;
import com.hospital.hms.employee.entity.EmployeeInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String fullName,
        LocalDate dob,
        Gender gender,
        String address,
        String phone,
        boolean isActive,
        String role,
        DepartmentResponse department,
        LocalDate hireDate,
        String code,
        LocalDateTime createdAt,
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
