package com.hospital.hms.billing.repository;

import com.hospital.hms.billing.entity.MedicineInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicineInvoiceRepository extends JpaRepository<MedicineInvoice, UUID> {

    List<MedicineInvoice> findByMedicalRecord_Id(UUID medicalRecordId);
}
