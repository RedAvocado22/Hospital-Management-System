package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.exception.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Query parameters for searching medical records")
public class SearchMedicalRecordRequest extends PaginatedRequest {

    @Schema(
            description = "Full-text keyword search on patient name and phone number",
            example = "John 0912"
    )
    private String keyword;

    @Schema(
            description = "Partial match (LIKE) on the attending doctor's full name",
            example = "Nguyen"
    )
    private String doctorName;

    @Schema(
            description = "Start of the date range filter (ISO-8601, inclusive). Must be provided together with 'to'.",
            example = "2025-01-01"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @Schema(
            description = "End of the date range filter (ISO-8601, inclusive). Must be provided together with 'from'.",
            example = "2025-12-31"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    @Override
    public void validate() {
        if ((from == null) != (to == null)) {
            throw new ValidationException("Invalid date range");
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Invalid date range");
        }
    }
}
