package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.PaginatedRequest;
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
//@Schema(description = "Search field for medical record")
public class SearchMedicalRecordRequest extends PaginatedRequest {

    private String keyword;

    private String doctorName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    
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
