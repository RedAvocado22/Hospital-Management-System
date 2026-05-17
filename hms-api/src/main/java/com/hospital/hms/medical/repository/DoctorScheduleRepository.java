package com.hospital.hms.medical.repository;

import com.hospital.hms.medical.entity.DoctorSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface
DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    List<DoctorSchedule> findByDoctor_Id(UUID doctorId);

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.role.name = 'doctor' AND ds.doctor.id = :doctorId AND ds.date = :date ")
    Page<DoctorSchedule> findByDoctorAndDateWithRoleCheck(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            Pageable pageable
    );

    boolean existsByDoctor_IdAndDoctor_Role_Name(UUID doctorId, String roleName);

    Optional<DoctorSchedule> findByDoctor_IdAndDateAndStartTimeAndEndTime(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);

    Page<DoctorSchedule> findByDate(LocalDate date, Pageable pageable);
}
