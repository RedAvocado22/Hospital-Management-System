package com.hospital.hms.patient.dto;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.patient.entity.PatientInfo;

import java.time.LocalDate;
import java.util.UUID;

public record PatientSummary(
        UUID id,
        String fullName,
        Gender gender,
        LocalDate dob,
        String address,
        String phone,
        String email,
        String bloodType,
        String allergies
) {
    public static PatientSummary from(PatientInfo patientInfo) {
        return new PatientSummary(
                patientInfo.getId(),
                patientInfo.getAccount().getFullName(),
                patientInfo.getAccount().getGender(),
                patientInfo.getAccount().getDob(),
                patientInfo.getAccount().getAddress(),
                patientInfo.getAccount().getPhone(),
                patientInfo.getAccount().getEmail(),
                patientInfo.getBloodType(),
                patientInfo.getAllergies()
        );
    }
}
