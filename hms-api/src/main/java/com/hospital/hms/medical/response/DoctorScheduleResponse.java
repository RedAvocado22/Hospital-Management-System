package com.hospital.hms.medical.response;

import com.hospital.hms.auth.entity.Account;

import java.time.LocalDate;
import java.time.LocalTime;

public record DoctorScheduleResponse(Account doctor,
                                     LocalDate date,
                                     LocalTime startTime,
                                     LocalTime endTime,
                                     Integer maxPatients,
                                     Boolean isAvailable) {
}
