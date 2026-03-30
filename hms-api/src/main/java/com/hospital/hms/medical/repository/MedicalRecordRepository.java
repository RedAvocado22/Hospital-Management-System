package com.hospital.hms.medical.repository;

import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.response.MedicalRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    @Override
    @EntityGraph(attributePaths = {"doctor", "patient", "patient.account"})
    Optional<MedicalRecord> findById(UUID id);

    @Query(
            value = "SELECT BIN_TO_UUID(mr.id) as id, " +
                    "mr.doctor_advice as doctor_advice, " +
                    "pa.full_name as patient_name, " +
                    "BIN_TO_UUID(p.id) as patient_id, da.full_name as doctor_name, " +
                    "BIN_TO_UUID(da.id) as doctor_id, " +
                    "mr.description as description, " +
                    "mr.created_at as created_at " +
                    "FROM medical_record mr " +
                    "JOIN patient_info p ON p.id = mr.patient_id \n" +
                    "JOIN account pa ON pa.id = p.account_id \n" +
                    "JOIN account da ON da.id = mr.doctor_id \n" +
                    "WHERE (:keyword IS NULL OR MATCH(pa.full_name, pa.phone) AGAINST (:keyword IN BOOLEAN MODE))\n" +
                    "AND (:doctorId IS NULL OR da.id = UUID_TO_BIN(:doctorId)) \n" +
                    "AND (:doctorName IS NULL OR LOWER(da.full_name) LIKE LOWER(CONCAT('%', :doctorName, '%')))\n" +
                    "AND (:from IS NULL OR mr.created_at >= :from)\n" +
                    "AND (:to IS NULL OR mr.created_at < :to)",
            countQuery = "SELECT COUNT(*) FROM medical_record mr " +
                    "JOIN patient_info p ON p.id = mr.patient_id \n" +
                    "JOIN account pa ON pa.id = p.account_id \n" +
                    "JOIN account da ON da.id = mr.doctor_id \n" +
                    "WHERE (:keyword IS NULL OR MATCH(pa.full_name, pa.phone) AGAINST (:keyword IN BOOLEAN MODE))\n" +
                    "AND (:doctorId IS NULL OR da.id = UUID_TO_BIN(:doctorId)) \n" +
                    "AND (:doctorName IS NULL OR LOWER(da.full_name) LIKE LOWER(CONCAT('%', :doctorName, '%')))\n" +
                    "AND (:from IS NULL OR mr.created_at >= :from)\n" +
                    "AND (:to IS NULL OR mr.created_at < :to)",
            nativeQuery = true
    )
    Page<MedicalRecordResponse> getMedicalRecordBy(
            @Param("keyword") String keyword,
            @Param("doctorName") String doctorName,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("doctorId") UUID doctorId,
            Pageable pageable
    );
}
