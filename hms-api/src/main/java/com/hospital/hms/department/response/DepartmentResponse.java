package com.hospital.hms.department.response;

import com.hospital.hms.department.entity.Department;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DepartmentResponse(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isActive
) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getCreatedAt(),
                department.getUpdatedAt(),
                department.isActive()
        );
    }
}
