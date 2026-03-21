package com.hospital.hms.medical.repository;

import com.hospital.hms.medical.entity.DoctorSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {
    
    Page<DoctorSchedule> findByDoctor_IdAndDate(UUID doctorId, LocalDate date, Pageable pageable);

    Page<DoctorSchedule> findByDate(LocalDate date, Pageable pageable);
}
