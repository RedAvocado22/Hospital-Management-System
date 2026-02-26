package com.hospital.hms.patient.repository;

import com.hospital.hms.patient.entity.MedicalExaminationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalExaminationHistoryRepository extends JpaRepository<MedicalExaminationHistory, UUID> {

    List<MedicalExaminationHistory> findByPatient_Id(UUID patientId);
}
