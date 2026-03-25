package com.hospital.hms.medical.response;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MedicalRecordResponse {

    UUID getId();

    UUID getPatientId();

    String getPatientName();

    String getDoctorName();

    UUID getDoctorId();

    @org.springframework.beans.factory.annotation.Value("#{target.doctorAdvice}")
    String getAdvice();

    String getDescription();

    LocalDateTime getCreatedAt();
}
