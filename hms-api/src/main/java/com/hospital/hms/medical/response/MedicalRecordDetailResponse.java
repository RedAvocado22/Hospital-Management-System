package com.hospital.hms.medical.response;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.patient.entity.PatientInfo;

import java.time.LocalDate;

public record MedicalRecordDetailResponse(
        PatientInfoResponse patient,
        DoctorSummary doctor,
        String description,
        String doctorAdvice

) {
    public record PatientInfoResponse(
            String fullName,
            Gender gender,
            LocalDate dob,
            String address,
            String phone,
            String email,
            String bloodType,
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

    public record DoctorSummary(
            String fullName,
            String departmentName,
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

