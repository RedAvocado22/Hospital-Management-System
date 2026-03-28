package com.hospital.hms.appointment.response;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.request.BookAppointmentRequest;
import com.hospital.hms.common.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID doctorId,
        UUID patientId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String reason,
        AppointmentStatus status
) {
    public static AppointmentResponse from(Appointment appointment, BookAppointmentRequest request) {
        return new AppointmentResponse(
                appointment.getId(),
                request.getDoctorId(),
                request.getPatientId(),
                appointment.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }
}
