package com.hospital.hms.billing.repository;

import com.hospital.hms.billing.entity.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HospitalServiceRepository extends JpaRepository<HospitalService, UUID> {
}
