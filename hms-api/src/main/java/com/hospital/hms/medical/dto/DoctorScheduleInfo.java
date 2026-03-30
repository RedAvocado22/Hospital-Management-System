package com.hospital.hms.medical.dto;

import com.hospital.hms.medical.entity.DoctorSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record DoctorScheduleInfo(
        UUID id,
        UUID doctorId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int maxPatients,
        boolean isAvailable
) {
    public static DoctorScheduleInfo from(DoctorSchedule schedule) {
        return new DoctorScheduleInfo(
                schedule.getId(),
                schedule.getDoctor().getId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getMaxPatients(),
                schedule.getIsAvailable()
        );
    }
}
