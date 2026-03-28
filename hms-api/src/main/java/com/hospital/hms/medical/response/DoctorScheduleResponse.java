package com.hospital.hms.medical.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record DoctorScheduleResponse(UUID doctorId,
                                     String fullName,
                                     LocalDate date,
                                     LocalTime startTime,
                                     LocalTime endTime,
                                     Integer maxPatients,
                                     Boolean isAvailable) {
}
