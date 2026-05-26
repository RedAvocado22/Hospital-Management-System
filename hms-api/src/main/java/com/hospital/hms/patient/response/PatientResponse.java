package com.hospital.hms.patient.response;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.patient.entity.PatientInfo;

import java.util.UUID;

public record PatientResponse(
        UUID id,
        String email,
        String fullName,
        String phone,
        boolean active
) {
    public static PatientResponse from(PatientInfo patient, AccountInfo account) {
        return new PatientResponse(
                patient.getId(),
                account.email(),
                account.fullName(),
                account.phone(),
                account.active()
        );
    }
}
