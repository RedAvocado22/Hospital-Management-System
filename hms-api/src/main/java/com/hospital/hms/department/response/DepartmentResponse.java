package com.hospital.hms.department.response;

import com.hospital.hms.department.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Department details")
public record DepartmentResponse(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Department name", example = "Cardiology")
        String name,

        @Schema(description = "Record creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last modification timestamp")
        LocalDateTime updatedAt,

        @Schema(description = "Whether the department is currently active", example = "true")
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
