package com.hospital.hms.pharmacy.repository;

import com.hospital.hms.pharmacy.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID>,
        JpaSpecificationExecutor<Medicine> {
    Optional<Medicine> findById(UUID id);
}
