package com.hospital.hms.medical.response;

import com.hospital.hms.medical.entity.MedicalRecord;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MedicalRecordResponse(
        UUID id,
        String patientName,
        String doctorName,
        String advice,
        String description,
        LocalDateTime createdAt
) {
    public static MedicalRecordResponse from(MedicalRecord medicalRecord) {
        return new MedicalRecordResponse(
                medicalRecord.getId(),
                medicalRecord.getPatient().getAccount().getFullName(),
                medicalRecord.getDoctor().getFullName(),
                medicalRecord.getDoctorAdvice(),
                medicalRecord.getDescription(),
                medicalRecord.getCreatedAt()
        );
    }
}
