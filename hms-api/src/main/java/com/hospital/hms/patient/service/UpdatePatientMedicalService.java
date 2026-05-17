package com.hospital.hms.patient.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import com.hospital.hms.patient.request.UpdatePatientMedicalRequest;
import com.hospital.hms.patient.response.PatientDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePatientMedicalService extends BaseService<UpdatePatientMedicalRequest, PatientDetailResponse> {

    private final PatientInfoRepository patientInfoRepository;

    @Override
    @Transactional
    public PatientDetailResponse execute(UpdatePatientMedicalRequest request) {
        return super.execute(request);
    }

    @Override
    protected PatientDetailResponse doProcess(UpdatePatientMedicalRequest request) {
        log.info("Processing medical profile update for patient ID: {}", request.getPatientId());

        PatientInfo patient = patientInfoRepository.findWithAccountById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + request.getPatientId()));

        if (request.getBloodType() != null) patient.setBloodType(request.getBloodType());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());

        patientInfoRepository.save(patient);
        log.info("Successfully updated medical profile for patient ID: {}", request.getPatientId());
        return PatientDetailResponse.from(patient, AccountInfo.from(patient.getAccount()));
    }
}
