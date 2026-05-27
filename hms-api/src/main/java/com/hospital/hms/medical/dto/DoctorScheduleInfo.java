package com.hospital.hms.medical.dto;

import com.hospital.hms.common.enums.ShiftType;
import com.hospital.hms.medical.entity.DoctorSchedule;

import java.time.LocalDate;
import java.util.UUID;

public record DoctorScheduleInfo(
        UUID id,
        UUID doctorId,
        LocalDate date,
        ShiftType type,
        int maxPatients,
        boolean isAvailable
) {
    public static DoctorScheduleInfo from(DoctorSchedule schedule) {
        return new DoctorScheduleInfo(
                schedule.getId(),
                schedule.getDoctor().getId(),
                schedule.getDate(),
                schedule.getType(),
                schedule.getMaxPatients(),
                schedule.getIsAvailable()
        );
    }
}
