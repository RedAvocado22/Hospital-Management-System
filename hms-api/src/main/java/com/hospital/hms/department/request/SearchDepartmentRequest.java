package com.hospital.hms.department.request;

import com.hospital.hms.base.request.PaginatedRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SearchDepartmentRequest extends PaginatedRequest {
    private String name;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
