package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.repository.AppointmentRepository;
import com.hospital.hms.appointment.request.AppointmentIdRequest;
import com.hospital.hms.appointment.response.AppointmentResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConfirmAppointmentService extends BaseService<AppointmentIdRequest, AppointmentResponse> {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public AppointmentResponse execute(AppointmentIdRequest request) {
        return super.execute(request);
    }

    @Override
    protected AppointmentResponse doProcess(AppointmentIdRequest request) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(request.getId()).orElseThrow(
                () -> new NotFoundException("Appointment not found")
        );

        if (request.getUserContext().hasRole("ROLE_DOCTOR")
                && !request.getUserContext().getUserId().equals(appointment.getDoctor().getId())) {
            throw new AccessDeniedException("You do not have permission to perform this action");
        }

        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Can't confirm the appointment in the past");
        }

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new BusinessException("Appointment only confirm when pending");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentResponse.from(saved);
    }
}
