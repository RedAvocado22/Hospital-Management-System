package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.hospital.hms.common.enums.AppointmentStatus.CONFIRMED;
import static com.hospital.hms.common.enums.AppointmentStatus.PENDING;

@Service
@RequiredArgsConstructor
public class AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;

    public boolean hasActiveAppointments(UUID scheduleId) {
        return appointmentRepository.existsBySchedule_IdAndStatusIn(scheduleId, List.of(PENDING, CONFIRMED));
    }
}
