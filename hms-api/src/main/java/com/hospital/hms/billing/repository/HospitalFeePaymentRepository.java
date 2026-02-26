package com.hospital.hms.billing.repository;

import com.hospital.hms.billing.entity.HospitalFeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HospitalFeePaymentRepository extends JpaRepository<HospitalFeePayment, UUID> {

    List<HospitalFeePayment> findByPatient_Id(UUID patientId);
}
