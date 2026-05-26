package com.hospital.hms.medical.service;

import com.hospital.hms.appointment.service.AppointmentQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.repository.DoctorScheduleRepository;
import com.hospital.hms.medical.request.DoctorScheduleIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeactivateDoctorScheduleService extends BaseService<DoctorScheduleIdRequest, Void> {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentQueryService appointmentQueryService;

    @Override
    @Transactional
    public Void execute(DoctorScheduleIdRequest request) {
        return super.execute(request);
    }

    @Override
    protected Void doProcess(DoctorScheduleIdRequest request) {
        DoctorSchedule ds = doctorScheduleRepository.findDetailsById(request.getId()).orElseThrow(
                () -> new NotFoundException("Doctor schedule not found")
        );

        if (Boolean.FALSE.equals(ds.getIsAvailable())) {
            throw new BusinessException("Schedule is already deactivated");
        }

        if (appointmentQueryService.hasActiveAppointments(request.getId())) {
            throw new BusinessException("Schedule can't be deactivated");
        }

        if (LocalDateTime.of(ds.getDate(), ds.getType().getStart()).isBefore(LocalDateTime.now().plusDays(1))) {
            throw new BusinessException("Schedule can't be deactivated");
        }

        ds.setIsAvailable(false);
        doctorScheduleRepository.save(ds);

        return null;
    }

}
