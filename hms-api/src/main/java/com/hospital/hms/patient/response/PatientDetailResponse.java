package com.hospital.hms.patient.response;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.patient.entity.PatientInfo;

import java.time.LocalDate;
import java.util.UUID;

public record PatientDetailResponse(
        UUID id,
        String username,
        String email,
        String fullName,
        LocalDate dob,
        Gender gender,
        String address,
        String phone,
        boolean active,
        String bloodType,
        String allergies
) {
    public static PatientDetailResponse from(PatientInfo patient, AccountInfo account) {
        return new PatientDetailResponse(
                patient.getId(),
                account.username(),
                account.email(),
                account.fullName(),
                account.dob(),
                account.gender(),
                account.address(),
                account.phone(),
                account.active(),
                patient.getBloodType(),
                patient.getAllergies()
        );
    }
}
