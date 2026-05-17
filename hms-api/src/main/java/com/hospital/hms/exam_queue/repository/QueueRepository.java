package com.hospital.hms.exam_queue.repository;

import com.hospital.hms.exam_queue.entity.QueueInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueueRepository extends JpaRepository<QueueInfo, UUID> {

    Page<QueueInfo> findByDate(LocalDate date, Pageable pageable);

    boolean existsByDateAndPatientId(LocalDate date, UUID patientId);

    Integer countByDate(LocalDate date);

    @Override
    Optional<QueueInfo> findById(UUID id);
}
