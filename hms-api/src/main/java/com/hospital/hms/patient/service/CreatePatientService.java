package com.hospital.hms.patient.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.auth.request.AccountRegistrationRequest;
import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.auth.service.AccountRegistrationService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import com.hospital.hms.patient.request.CreatePatientRequest;
import com.hospital.hms.patient.response.PatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePatientService extends BaseService<CreatePatientRequest, PatientResponse> {

    private final PatientInfoRepository patientInfoRepository;
    private final AccountRegistrationService accountRegistrationService;
    private final AccountQueryService accountQueryService;

    @Override
    protected PatientResponse doProcess(CreatePatientRequest request) {
        log.info("Attempting to create patient with username: {}", request.getUsername());

        AccountRegistrationRequest accountRegistrationRequest = AccountRegistrationRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .role("Patient")
                .build();

        AccountInfo accountInfo = accountRegistrationService.execute(accountRegistrationRequest);

        PatientInfo info = PatientInfo.builder()
                .account(accountQueryService.getReferenceById(accountInfo.id()))
                .bloodType(request.getBloodType())
                .allergies(request.getAllergies())
                .build();

        PatientInfo saved = patientInfoRepository.save(info);

        log.debug("Patient info saved to local database");
        log.info("Patient {} created successfully", request.getUsername());

        return PatientResponse.from(saved, accountInfo);
    }

    @Override
    @Transactional
    public PatientResponse execute(CreatePatientRequest request) {
        return super.execute(request);
    }
}
