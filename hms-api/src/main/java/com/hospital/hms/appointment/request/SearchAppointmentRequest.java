package com.hospital.hms.appointment.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.exception.ValidationException;
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
public class SearchAppointmentRequest extends PaginatedRequest {
    private String doctorName;

    private String patientName;
    private String patientPhone;
    private String patientEmail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private AppointmentStatus status;

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
