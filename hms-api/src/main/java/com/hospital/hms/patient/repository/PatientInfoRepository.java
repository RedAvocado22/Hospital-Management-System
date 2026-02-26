package com.hospital.hms.patient.repository;

import com.hospital.hms.patient.entity.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, UUID> {

    Optional<PatientInfo> findByAccount_Id(UUID accountId);
}
