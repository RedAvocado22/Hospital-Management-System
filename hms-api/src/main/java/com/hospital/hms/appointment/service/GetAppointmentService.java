package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.repository.AppointmentRepository;
import com.hospital.hms.appointment.repository.AppointmentSpecification;
import com.hospital.hms.appointment.request.SearchAppointmentRequest;
import com.hospital.hms.appointment.response.AppointmentResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.patient.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAppointmentService extends BaseService<SearchAppointmentRequest, PaginatedResponse<AppointmentResponse>> {

    private final PatientQueryService patientQueryService;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponse> execute(SearchAppointmentRequest request) {
        return super.execute(request);
    }

    @Override
    protected PaginatedResponse<AppointmentResponse> doProcess(SearchAppointmentRequest request) {
        UUID doctorId = null;
        UUID patientId = null;

        if (request.getUserContext().hasRole("ROLE_DOCTOR")) {
            doctorId = request.getUserContext().getUserId();
        }

        if (request.getUserContext().hasRole("ROLE_PATIENT")) {
            patientId = patientQueryService.getPatientIdByAccountId(request.getUserContext().getUserId());
        }

        Pageable pageable = request.toPageable();
        Specification<Appointment> spec = AppointmentSpecification.withFilters(request, doctorId, patientId);

        Page<Appointment> appointments = appointmentRepository.findAll(spec, pageable);

        Page<AppointmentResponse> response = appointments.map(appointment -> AppointmentResponse.fromWithDetails(appointment));

        return PaginatedResponse.from(response);
    }
}
