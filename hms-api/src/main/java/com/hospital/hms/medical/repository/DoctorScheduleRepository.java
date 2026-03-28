package com.hospital.hms.medical.repository;

import com.hospital.hms.medical.entity.DoctorSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface
DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    List<DoctorSchedule> findByDoctor_Id(UUID doctorId);

    Page<DoctorSchedule> findByDoctor_IdAndDate(UUID doctorId, LocalDate date, Pageable pageable);

    Optional<DoctorSchedule> findByDoctor_IdAndDateAndStartTimeAndEndTime(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);

    Page<DoctorSchedule> findByDate(LocalDate date, Pageable pageable);
}
