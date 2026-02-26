package com.hospital.hms.appointment.repository;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.common.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByPatient_Id(UUID patientId);

    List<Appointment> findByDoctor_Id(UUID doctorId);

    List<Appointment> findByStatus(AppointmentStatus status);
}
