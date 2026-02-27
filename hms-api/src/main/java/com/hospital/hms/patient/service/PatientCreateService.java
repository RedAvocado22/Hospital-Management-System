package com.hospital.hms.patient.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.patient.dto.request.PatientCreateRequest;
import com.hospital.hms.patient.dto.response.PatientResponse;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.mapper.PatientInfoMapper;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientCreateService implements BaseService<PatientCreateRequest, PatientResponse> {

    private final PatientInfoRepository patientInfoRepository;
    private final AccountRepository accountRepository;
    private final PatientInfoMapper patientInfoMapper;

    @Override
    @Transactional
    public PatientResponse doProcess(PatientCreateRequest request) {

        log.info("Creating patient info for accountId={}", request.getAccountId());

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (patientInfoRepository.findByAccount_Id(account.getId()).isPresent()) {
            throw new IllegalStateException("Patient info already exists for this account");
        }

        PatientInfo patientInfo = patientInfoMapper.toEntity(request);
        patientInfo.setAccount(account);

        patientInfoRepository.save(patientInfo);

        log.info("Patient info created successfully with id={}", patientInfo.getId());

        return patientInfoMapper.toResponse(patientInfo);
    }

    @Override
    public void validate(PatientCreateRequest request) {
        BaseService.super.validate(request);

        if (request.getAccountId() == null) {
            throw new IllegalArgumentException("AccountId must not be null");
        }
    }

    @Override
    public PatientResponse execute(PatientCreateRequest request) {
        return BaseService.super.execute(request);
    }

    @Override
    public Optional<PatientResponse> executeOptional(PatientCreateRequest request) {
        return BaseService.super.executeOptional(request);
    }

    @Override
    public Optional<PatientResponse> executeSilent(PatientCreateRequest request) {
        return BaseService.super.executeSilent(request);
    }
}