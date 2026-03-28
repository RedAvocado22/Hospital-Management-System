package com.hospital.hms.medical.service;

import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.repository.DoctorScheduleRepository;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorScheduleQueryService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    @Transactional(readOnly = true)
    public DoctorScheduleDetailResponse findDoctorSchedule(
            UUID doctorId, LocalDate date,
            LocalTime startTime, LocalTime endTime
    ) {
        DoctorSchedule ds = doctorScheduleRepository.findByDoctor_IdAndDateAndStartTimeAndEndTime(
                doctorId, date, startTime, endTime
        ).orElseThrow(
                () -> new NotFoundException("Doctor schedule not found")
        );

        return DoctorScheduleDetailResponse.from(ds);
    }

    public DoctorSchedule getReferenceById(UUID doctorScheduleId) {
        return doctorScheduleRepository.getReferenceById(doctorScheduleId);
    }
}
