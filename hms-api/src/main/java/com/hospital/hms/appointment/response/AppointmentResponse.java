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
        String doctorName,
        UUID patientId,
        String patientName,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status
) {
    public static AppointmentResponse from(Appointment appointment, BookAppointmentRequest request) {
        return new AppointmentResponse(
                appointment.getId(),
                request.getDoctorId(),
                null,
                request.getPatientId(),
                null,
                appointment.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                appointment.getStatus()
        );
    }

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                null,
                appointment.getPatient().getId(),
                null,
                appointment.getDate(),
                appointment.getSchedule().getStartTime(),
                appointment.getSchedule().getEndTime(),
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
                appointment.getSchedule().getStartTime(),
                appointment.getSchedule().getEndTime(),
                appointment.getStatus()
        );
    }
}
