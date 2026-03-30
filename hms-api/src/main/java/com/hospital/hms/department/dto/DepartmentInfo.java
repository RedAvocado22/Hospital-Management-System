package com.hospital.hms.department.dto;

import com.hospital.hms.department.entity.Department;

import java.util.UUID;

public record DepartmentInfo(
        UUID id,
        String name,
        boolean isActive
) {
    public static DepartmentInfo from(Department department) {
        return new DepartmentInfo(
                department.getId(),
                department.getName(),
                department.isActive()
        );
    }
}
