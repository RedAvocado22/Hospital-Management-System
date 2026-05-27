package com.hospital.hms.appointment.response;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.common.enums.ShiftType;

import java.time.LocalDate;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID doctorId,
        String doctorName,
        UUID patientId,
        String patientName,
        LocalDate date,
        ShiftType shift,
        AppointmentStatus status
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                null,
                appointment.getPatient().getId(),
                null,
                appointment.getDate(),
                appointment.getSchedule().getType(),
                appointment.getStatus()
        );
    }

    public static AppointmentResponse fromWithDetails(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getFullName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getAccount().getFullName(),
                appointment.getDate(),
                appointment.getSchedule().getType(),
                appointment.getStatus()
        );
    }
}
