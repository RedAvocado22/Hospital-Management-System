package com.hospital.hms.appointment.repository;

import com.hospital.hms.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {

    @Override
    @EntityGraph(attributePaths = {"doctor", "schedule", "patient", "patient.account"})
    Page<Appointment> findAll(Specification<Appointment> spec, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.id = :id")
    @EntityGraph(attributePaths = {"doctor", "schedule", "patient", "patient.account"})
    Optional<Appointment> findByIdWithDetails(@Param("id") UUID id);

    Integer countAppointmentByDoctorSchedule_Id(UUID doctorScheduleId);
}
