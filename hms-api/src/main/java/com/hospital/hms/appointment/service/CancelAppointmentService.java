package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.repository.AppointmentRepository;
import com.hospital.hms.appointment.request.AppointmentIdRequest;
import com.hospital.hms.appointment.response.AppointmentResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.common.enums.AppointmentStatus;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelAppointmentService extends BaseService<AppointmentIdRequest, AppointmentResponse> {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotService appointmentSlotService;

    @Override
    @Transactional
    public AppointmentResponse execute(AppointmentIdRequest request) {
        return super.execute(request);
    }


    @Override
    protected AppointmentResponse doProcess(AppointmentIdRequest request) {
        log.info("Cancelling appointment — id: {}", request.getId());

        Appointment appointment = appointmentRepository.findByIdWithDetails(request.getId()).orElseThrow(
                () -> new NotFoundException("Appointment not found")
        );

        if (request.getUserContext().hasRole("ROLE_PATIENT")) {
            UUID patientAccountId = appointment.getPatient().getAccount().getId();
            if (!patientAccountId.equals(request.getUserContext().getUserId())) {
                log.warn("ABAC denial — patient {} attempted to cancel appointment {} owned by {}",
                        request.getUserContext().getUserId(), request.getId(), patientAccountId);
                throw new AccessDeniedException("You do not have permission to do this");
            }
        }

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING) && !appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
            throw new BusinessException("Appointment only cancel when pending or confirmed");
        }

        String key = "slots:" + appointment.getDoctor().getId() + ":" + appointment.getDate() + ":" + appointment.getSchedule().getStartTime() + "-" + appointment.getSchedule().getEndTime();
        boolean slotReleased = appointmentSlotService.cancelSlot(key);
        if (!slotReleased) {
            log.warn("Redis slot key not found or already at 0 — key: {}. Proceeding with DB cancel.", key);
        } else {
            log.debug("Redis slot decremented — key: {}", key);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        log.info("Appointment cancelled — id: {}", saved.getId());
        return AppointmentResponse.from(saved);
    }
}
