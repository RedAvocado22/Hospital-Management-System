package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.repository.AppointmentRepository;
import com.hospital.hms.appointment.request.BookAppointmentRequest;
import com.hospital.hms.appointment.response.BookAppointmentResponse;
import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.ValidationException;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
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
public class BookAppointmentService extends BaseService<BookAppointmentRequest, BookAppointmentResponse> {

    private final DoctorScheduleQueryService doctorScheduleQueryService;
    private final AppointmentSlotService appointmentSlotService;
    private final AppointmentRepository appointmentRepository;
    private final AccountQueryService accountQueryService;
    private final PatientQueryService patientQueryService;

    @Override
    @Transactional
    public BookAppointmentResponse execute(BookAppointmentRequest request) {
        return super.execute(request);
    }

    @Override
    protected BookAppointmentResponse doProcess(BookAppointmentRequest request) {
        log.info("Booking appointment — doctorId: {}, patientId: {}, date: {}",
                request.getDoctorId(), request.getPatientId(), request.getDate());

        DoctorScheduleDetailResponse doctorSchedule = doctorScheduleQueryService.findDoctorSchedule(
                request.getDoctorId(), request.getDate(), request.getStartTime(), request.getEndTime()
        );

        if (!doctorSchedule.isAvailable()) {
            throw new BusinessException("Doctor is not available");
        }

        String key = "slots:" + request.getDoctorId() + ":" + request.getDate() + ":" + request.getStartTime() + "-" + request.getEndTime();

        if (appointmentSlotService.bookSlot(doctorSchedule.id(), key, doctorSchedule.maxPatients())) {
            Appointment appointment = Appointment.builder()
                    .doctor(accountQueryService.getReferenceById(request.getDoctorId()))
                    .date(request.getDate())
                    .patient(patientQueryService.getReferenceById(request.getPatientId()))
                    .schedule(doctorScheduleQueryService.getReferenceById(doctorSchedule.id()))
                    .reason(request.getReason())
                    .status(AppointmentStatus.PENDING)
                    .build();
            appointmentRepository.save(appointment);

            log.info("Appointment booked, id: {}", appointment.getId());

            return BookAppointmentResponse.from(appointment, request);
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

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Date must not be in the past");
        }

        if (request.getDate().isEqual(LocalDate.now()) && request.getStartTime().isBefore(LocalTime.now())) {
            throw new ValidationException("Start time cannot be in the past");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new ValidationException("Start time must be before end time");
        }

        if (request.getUserContext().hasRole("ROLE_RECEPTIONIST") && request.getPatientId() == null) {
            throw new ValidationException("Patient ID must be specified for this book appointment");
        } else if (request.getUserContext().hasRole("ROLE_PATIENT")) {
            request.setPatientId(patientQueryService.getPatientIdByAccountId(request.getUserContext().getUserId()));
        }
    }
}
