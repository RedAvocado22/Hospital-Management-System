package com.hospital.hms.medical.service;

import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordQueryService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord getReferenceById(UUID id) {
        return medicalRecordRepository.getReferenceById(id);
    }
}
