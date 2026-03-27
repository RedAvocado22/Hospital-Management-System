package com.hospital.hms.medical.repository;

import com.hospital.hms.medical.entity.DoctorSchedule;
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

    List<DoctorSchedule> findByDoctor_IdAndDate(UUID doctorId, LocalDate date);

    Optional<DoctorSchedule> findByDoctor_IdAndDateAndStartTimeAndEndTime(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
