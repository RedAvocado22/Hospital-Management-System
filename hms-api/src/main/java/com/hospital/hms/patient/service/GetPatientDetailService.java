package com.hospital.hms.patient.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import com.hospital.hms.patient.request.PatientIdRequest;
import com.hospital.hms.patient.response.PatientDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPatientDetailService extends BaseService<PatientIdRequest, PatientDetailResponse> {

    private final PatientInfoRepository patientInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientDetailResponse execute(PatientIdRequest request) {
        return super.execute(request);
    }

    @Override
    protected PatientDetailResponse doProcess(PatientIdRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        log.debug("Fetching patient id: {} for user: {}",
                request.getId(), request.getUserContext().getUserId());

        PatientInfo patient = patientInfoRepository.findWithAccountById(request.getId()).orElseThrow(
                () -> new NotFoundException("Patient with id " + request.getId() + " not found")
        );

        if (request.getUserContext().hasRole("ROLE_PATIENT")
                && !request.getUserContext().getUserId().equals(patient.getAccount().getId())) {
            log.warn("Access denied: patient account {} attempted to view patient {}",
                    request.getUserContext().getUserId(), request.getId());
            throw new AccessDeniedException("You are not allowed to view this patient profile");
        }

        AccountInfo accountInfo = AccountInfo.from(patient.getAccount());
        return PatientDetailResponse.from(patient, accountInfo);
    }
}

