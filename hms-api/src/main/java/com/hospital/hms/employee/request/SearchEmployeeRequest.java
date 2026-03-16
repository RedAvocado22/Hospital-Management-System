package com.hospital.hms.employee.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.common.enums.Gender;
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
@Schema(description = "Query parameters for searching and filtering employees")
public class SearchEmployeeRequest extends PaginatedRequest {

    @Schema(description = "Filter by employee name (partial match)", example = "Nguyen")
    private String name;

    @Schema(description = "Filter by date of birth range")
    private DateRange dob;

    @Schema(description = "Filter by gender", example = "MALE")
    private Gender gender;

    @Schema(description = "Filter by address (partial match)", example = "Ho Chi Minh")
    private String address;

    @Schema(description = "Filter by phone number", example = "0901234567")
    private String phone;

    @Schema(description = "Filter by email address", example = "nguyen@hospital.com")
    private String email;

    @Schema(description = "Filter by hire date range")
    private DateRange hireDate;

    @Schema(description = "Filter by creation date range")
    private DateRange createdAtRange;

    @Schema(description = "Filter by last update date range")
    private DateRange updatedAtRange;

    @Schema(description = "Filter by department name", example = "Cardiology")
    private String department;

    @Schema(description = "Filter by employee code", example = "EMP-001")
    private String code;

    @Schema(description = "Filter by active status", example = "true")
    private Boolean isActive;
}
