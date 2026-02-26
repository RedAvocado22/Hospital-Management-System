package com.hospital.hms.pharmacy.repository;

import com.hospital.hms.pharmacy.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    Optional<Medicine> findById(UUID id);
}
