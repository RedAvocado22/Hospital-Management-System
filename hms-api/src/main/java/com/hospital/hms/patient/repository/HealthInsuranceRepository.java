package com.hospital.hms.patient.repository;

import com.hospital.hms.patient.entity.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, UUID> {

    List<HealthInsurance> findByPatient_Id(UUID patientId);
}
