package com.hospital.hms.department.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.common.model.DateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Query parameters for searching and filtering departments")
public class SearchDepartmentRequest extends PaginatedRequest {

    @Schema(description = "Filter by department name (partial match)", example = "Cardio")
    private String name;

    @Schema(description = "Filter by active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Filter by creation date range")
    private DateRange createdAtRange;

    @Schema(description = "Filter by last update date range")
    private DateRange updatedAtRange;
}
