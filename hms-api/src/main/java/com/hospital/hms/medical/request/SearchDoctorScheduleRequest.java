package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SearchDoctorScheduleRequest extends PaginatedRequest {

    private UUID doctorId;

    private String roleName;

    private LocalDate date;
}
