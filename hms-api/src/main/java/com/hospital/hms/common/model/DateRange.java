package com.hospital.hms.common.model;

import java.time.LocalDate;

public record DateRange(
        LocalDate from,
        LocalDate to
) {
}
