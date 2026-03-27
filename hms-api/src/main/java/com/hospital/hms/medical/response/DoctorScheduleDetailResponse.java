package com.hospital.hms.medical.response;

import com.hospital.hms.medical.entity.DoctorSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record DoctorScheduleDetailResponse(
        UUID id,
        String doctorName,
        UUID doctorId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer maxPatients,
        Boolean isAvailable
) {
    public static DoctorScheduleDetailResponse from(DoctorSchedule schedule) {
        return new DoctorScheduleDetailResponse(
                schedule.getId(),
                schedule.getDoctor().getFullName(),
                schedule.getDoctor().getId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getMaxPatients(),
                schedule.getIsAvailable()
        );
    }
}
