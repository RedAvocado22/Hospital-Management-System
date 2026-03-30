package com.hospital.hms.patient.repository;

import com.hospital.hms.patient.entity.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, UUID>, JpaSpecificationExecutor<PatientInfo> {

    Optional<PatientInfo> findByAccount_Id(UUID accountId);

    @EntityGraph(attributePaths = {"account"})
    Optional<PatientInfo> findWithAccountById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"account"})
    Page<PatientInfo> findAll(Specification<PatientInfo> spec, Pageable pageable);
}
