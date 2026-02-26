package com.hospital.hms.pharmacy.repository;

import com.hospital.hms.pharmacy.entity.MedicineItem;
import com.hospital.hms.pharmacy.entity.MedicineItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicineItemRepository extends JpaRepository<MedicineItem, MedicineItemId> {

    List<MedicineItem> findByMedicinePrescription_Id(UUID prescriptionId);
}
