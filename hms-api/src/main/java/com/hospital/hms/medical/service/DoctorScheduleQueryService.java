package com.hospital.hms.medical.service;

import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.dto.DoctorScheduleInfo;
import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorScheduleQueryService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    @Transactional(readOnly = true)
    public DoctorScheduleInfo findDoctorSchedule(
            UUID scheduleId
    ) {
        DoctorSchedule ds = doctorScheduleRepository.findDetailsById(scheduleId).orElseThrow(
                () -> new NotFoundException("Doctor schedule not found")
        );

        return DoctorScheduleInfo.from(ds);
    }

    public DoctorSchedule getReferenceById(UUID doctorScheduleId) {
        return doctorScheduleRepository.getReferenceById(doctorScheduleId);
    }
}
