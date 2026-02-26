package com.hospital.hms.billing.repository;

import com.hospital.hms.billing.entity.ServiceInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceInvoiceRepository extends JpaRepository<ServiceInvoice, UUID> {

    List<ServiceInvoice> findByMedicalRecord_Id(UUID medicalRecordId);
}
