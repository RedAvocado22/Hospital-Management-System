package com.hospital.hms.pharmacy.repository;

import com.hospital.hms.pharmacy.entity.MedicinePrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicinePrescriptionRepository extends JpaRepository<MedicinePrescription, UUID> {

    List<MedicinePrescription> findByMedicineInvoice_Id(UUID medicineInvoiceId);
}
