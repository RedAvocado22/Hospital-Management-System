package com.hospital.hms.medical.response;

import com.hospital.hms.common.enums.ShiftType;
import com.hospital.hms.medical.entity.DoctorSchedule;

import java.time.LocalDate;
import java.util.UUID;

public record DoctorScheduleDetailResponse(
        UUID id,
        String doctorName,
        UUID doctorId,
        LocalDate date,
        ShiftType type,
        Integer maxPatients,
        Boolean isAvailable
) {
    public static DoctorScheduleDetailResponse from(DoctorSchedule schedule) {
        return new DoctorScheduleDetailResponse(
                schedule.getId(),
                schedule.getDoctor().getFullName(),
                schedule.getDoctor().getId(),
                schedule.getDate(),
                schedule.getType(),
                schedule.getMaxPatients(),
                schedule.getIsAvailable()
        );
    }
}
