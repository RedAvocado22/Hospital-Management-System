package com.hospital.hms.employee.dto;

import com.hospital.hms.department.dto.DepartmentInfo;
import com.hospital.hms.employee.entity.EmployeeInfo;

import java.util.UUID;

public record EmployeeSummary(
        UUID id,
        String fullName,
        String code,
        DepartmentInfo department
) {
    public static EmployeeSummary from(EmployeeInfo employee) {
        return new EmployeeSummary(
                employee.getId(),
                employee.getAccount().getFullName(),
                employee.getCode(),
                DepartmentInfo.from(employee.getDepartment())
        );
    }
}
