package com.hospital.hms.medical.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Summary row returned in the paginated medical records list")
public interface MedicalRecordResponse {

    @Schema(description = "UUID of the medical record", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID getId();

    @Schema(description = "UUID of the patient this record belongs to", example = "7c9e6679-7425-40de-944b-e07fc1f90ae7")
    UUID getPatientId();

    @Schema(description = "Full name of the patient", example = "Nguyen Van A")
    String getPatientName();

    @Schema(description = "Full name of the attending doctor", example = "Dr. Tran Minh Duc")
    String getDoctorName();

    @Schema(description = "UUID of the attending doctor's account", example = "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d")
    UUID getDoctorId();

    @Schema(description = "Doctor's advice for the patient", example = "Rest for 3 days, avoid spicy food.")
    @org.springframework.beans.factory.annotation.Value("#{target.doctorAdvice}")
    String getAdvice();

    @Schema(description = "Clinical notes from the examination", example = "Patient presents with acute upper respiratory infection.")
    String getDescription();

    @Schema(description = "Timestamp when the record was created (ISO-8601)", example = "2025-03-26T09:30:00")
    LocalDateTime getCreatedAt();
}
