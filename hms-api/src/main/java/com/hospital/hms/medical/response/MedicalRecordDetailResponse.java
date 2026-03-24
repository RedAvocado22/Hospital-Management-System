package com.hospital.hms.medical.response;

import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.employee.entity.EmployeeInfo;
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
        public static DoctorSummary from(EmployeeInfo employeeInfo) {
            return new DoctorSummary(
                    employeeInfo.getAccount().getFullName(),
                    employeeInfo.getDepartment().getName(),
                    employeeInfo.getCode()
            );
        }
    }

    public static MedicalRecordDetailResponse from(MedicalRecord medicalRecord, EmployeeInfo employeeInfo) {
        return new MedicalRecordDetailResponse(
                PatientInfoResponse.from(medicalRecord.getPatient()),
                DoctorSummary.from(employeeInfo),
                medicalRecord.getDescription(),
                medicalRecord.getDoctorAdvice()
        );
    }
}

