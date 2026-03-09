package com.hospital.hms.common.model;

import com.hospital.hms.exception.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Date range filter with inclusive start and end dates")
public record DateRange(
        @Schema(description = "Start date (inclusive)", example = "2026-01-01")
        LocalDate from,

        @Schema(description = "End date (inclusive)", example = "2026-12-31")
        LocalDate to
) {
    public DateRange {
        if ((from == null) != (to == null)) {
            throw new ValidationException("Invalid date range");
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Invalid date range");
        }
    }
}
