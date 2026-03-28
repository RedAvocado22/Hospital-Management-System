package com.hospital.hms.medical.response;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.patient.entity.PatientInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Full detail view of a single medical record")
public record MedicalRecordDetailResponse(
        @Schema(description = "Slim patient profile at time of examination")
        PatientInfoResponse patient,
        @Schema(description = "Summary of the attending doctor")
        DoctorSummary doctor,
        @Schema(description = "Clinical notes and findings from the examination", example = "Patient presents with acute upper respiratory infection.")
        String description,
        @Schema(description = "Doctor's advice or treatment instructions", example = "Rest for 3 days, return if fever exceeds 38.5°C.")
        String advice

) {
    @Schema(description = "Slim patient profile included in medical record detail")
    public record PatientInfoResponse(
            @Schema(description = "Patient's full name", example = "Nguyen Van A")
            String fullName,
            @Schema(description = "Patient's gender", example = "MALE")
            Gender gender,
            @Schema(description = "Date of birth (ISO-8601)", example = "1995-06-15")
            LocalDate dob,
            @Schema(description = "Patient's home address", example = "123 Le Loi, District 1, Ho Chi Minh City")
            String address,
            @Schema(description = "Contact phone number", example = "0912345678")
            String phone,
            @Schema(description = "Email address", example = "nguyenvana@example.com")
            String email,
            @Schema(description = "Blood type (ABO/Rh)", example = "O+")
            String bloodType,
            @Schema(description = "Known allergies, comma-separated or free text", example = "Penicillin, shellfish")
            String allergies
    ) {
        public static PatientInfoResponse from(PatientInfo patientInfo) {
            return new PatientInfoResponse(
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

    @Schema(description = "Summary of the attending doctor included in medical record detail")
    public record DoctorSummary(
            @Schema(description = "Doctor's full name", example = "Dr. Tran Minh Duc")
            String fullName,
            @Schema(description = "Department the doctor belongs to", example = "Internal Medicine")
            String departmentName,
            @Schema(description = "Employee code of the doctor", example = "EMP-0042")
            String code
    ) {
        public static DoctorSummary from(EmployeeResponse employeeResponse) {
            return new DoctorSummary(
                    employeeResponse.fullName(),
                    employeeResponse.department().name(),
                    employeeResponse.code()
            );
        }
    }

    public static MedicalRecordDetailResponse from(MedicalRecord medicalRecord, EmployeeResponse employeeResponse) {
        return new MedicalRecordDetailResponse(
                PatientInfoResponse.from(medicalRecord.getPatient()),
                DoctorSummary.from(employeeResponse),
                medicalRecord.getDescription(),
                medicalRecord.getDoctorAdvice()
        );
    }
}

