package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.repository.AppointmentRepository;
import com.hospital.hms.appointment.request.BookAppointmentRequest;
import com.hospital.hms.appointment.response.AppointmentResponse;
import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.ValidationException;
import com.hospital.hms.medical.dto.DoctorScheduleInfo;
import com.hospital.hms.medical.service.DoctorScheduleQueryService;
import com.hospital.hms.patient.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookAppointmentService extends BaseService<BookAppointmentRequest, AppointmentResponse> {

    private final DoctorScheduleQueryService doctorScheduleQueryService;
    private final AppointmentSlotService appointmentSlotService;
    private final AppointmentRepository appointmentRepository;
    private final AccountQueryService accountQueryService;
    private final PatientQueryService patientQueryService;

    @Override
    @Transactional
    public AppointmentResponse execute(BookAppointmentRequest request) {
        return super.execute(request);
    }

    @Override
    protected AppointmentResponse doProcess(BookAppointmentRequest request) {
        log.info("Booking appointment — doctor schedule with id: {}", request.getScheduleId());

        DoctorScheduleInfo schedule = doctorScheduleQueryService.findDoctorSchedule(request.getScheduleId());

        if (!schedule.isAvailable()) {
            throw new BusinessException("Doctor is not available");
        }

        if (schedule.date().isBefore(LocalDate.now())) {
            throw new ValidationException("Date must not be in the past");
        }

        if (schedule.date().isEqual(LocalDate.now()) && schedule.startTime().isBefore(LocalTime.now())) {
            throw new ValidationException("Start time cannot be in the past");
        }

        if (schedule.startTime().isAfter(schedule.endTime())) {
            throw new ValidationException("Start time must be before end time");
        }

        String key = "slots:" + schedule.doctorId() + ":" + schedule.date() + ":" + schedule.startTime() + "-" + schedule.endTime();

        if (appointmentSlotService.bookSlot(schedule.id(), key, schedule.maxPatients())) {
            Appointment appointment = Appointment.builder()
                    .doctor(accountQueryService.getReferenceById(schedule.doctorId()))
                    .date(schedule.date())
                    .patient(patientQueryService.getReferenceById(request.getPatientId()))
                    .schedule(doctorScheduleQueryService.getReferenceById(schedule.id()))
                    .reason(request.getReason())
                    .status(AppointmentStatus.PENDING)
                    .build();
            Appointment saved = appointmentRepository.save(appointment);

            log.info("Appointment booked, id: {}", saved.getId());

            return AppointmentResponse.from(saved);
        } else {
            log.warn("Slot fully booked — key: {}", key);
            throw new BusinessException("This schedule is fully booked");
        }
    }

    @Override
    protected void validate(BookAppointmentRequest request) {
        super.validate(request);

        if (request.getUserContext() == null) {
            throw new ValidationException("User context is missing");
        }

        if (request.getUserContext().hasRole("ROLE_RECEPTIONIST") && request.getPatientId() == null) {
            throw new ValidationException("Patient ID must be specified for this book appointment");
        } else if (request.getUserContext().hasRole("ROLE_PATIENT")) {
            request.setPatientId(patientQueryService.getPatientIdByAccountId(request.getUserContext().getUserId()));
        }
    }
}
