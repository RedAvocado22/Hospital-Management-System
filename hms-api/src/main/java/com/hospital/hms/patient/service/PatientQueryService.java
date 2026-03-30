package com.hospital.hms.patient.service;

import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.patient.dto.PatientSummary;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientQueryService {

    private final PatientInfoRepository patientInfoRepository;

    @Transactional(readOnly = true)
    public PatientInfo getReferenceById(UUID id) {
        if (!patientInfoRepository.existsById(id)) {
            throw new NotFoundException("Patient with id " + id + " not found");
        }
        return patientInfoRepository.getReferenceById(id);
    }

    public PatientSummary getPatientIdByAccountId(UUID id) {
        PatientInfo pi = patientInfoRepository.findByAccount_Id(id).orElseThrow(
                () -> new NotFoundException("Patient with id " + id + " not found")
        );
        return PatientSummary.from(pi);
    }

    @Transactional(readOnly = true)
    public PatientSummary getById(UUID id) {
        PatientInfo pi = patientInfoRepository.findWithAccountById(id).orElseThrow(
                () -> new NotFoundException("Patient with id " + id + " not found")
        );
        return PatientSummary.from(pi);
    }
}
