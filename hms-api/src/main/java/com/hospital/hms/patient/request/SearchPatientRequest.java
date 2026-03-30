package com.hospital.hms.patient.request;

import com.hospital.hms.base.request.PaginatedRequest;
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
@Schema(description = "Query parameters for searching patients")
public class SearchPatientRequest extends PaginatedRequest {

    @Schema(description = "Filter by patient full name (partial match)", example = "Nguyen")
    private String fullName;

    @Schema(description = "Filter by email address", example = "patient@example.com")
    private String email;

    @Schema(description = "Filter by phone number", example = "0901234567")
    private String phone;
}

