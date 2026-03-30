package com.hospital.hms.patient.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.keycloak.service.KeycloakService;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import com.hospital.hms.patient.request.UpdatePatientRequest;
import com.hospital.hms.patient.response.PatientDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePatientService extends BaseService<UpdatePatientRequest, PatientDetailResponse> {

    private final PatientInfoRepository patientInfoRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public PatientDetailResponse execute(UpdatePatientRequest request) {
        return super.execute(request);
    }

    @Override
    protected PatientDetailResponse doProcess(UpdatePatientRequest request) {
        log.info("Processing profile update for patient ID: {}", request.getPatientId());

        PatientInfo patient = patientInfoRepository.findWithAccountById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + request.getPatientId()));

        Account account = patient.getAccount();

        if (request.getUserContext() == null || !request.getUserContext().getUserId().equals(account.getId())) {
            log.warn("Access denied: User {} attempted to update patient profile {}",
                    request.getUserContext() != null ? request.getUserContext().getUserId() : "anonymous", account.getId());
            throw new AccessDeniedException("You are not allowed to update this patient profile");
        }

        if (request.getEmail() != null) account.setEmail(request.getEmail());
        if (request.getFirstName() != null) account.setFirstName(request.getFirstName());
        if (request.getLastName() != null) account.setLastName(request.getLastName());
        if (request.getDob() != null) account.setDob(request.getDob());
        if (request.getGender() != null) account.setGender(request.getGender());
        if (request.getAddress() != null) account.setAddress(request.getAddress());
        if (request.getPhone() != null) account.setPhone(request.getPhone());

        if (request.getPassword() != null) {
            log.info("Updating password in Keycloak for user ID: {}", account.getId());
            keycloakService.updatePassword(account.getId().toString(), request.getPassword());
        }

        log.info("Successfully updated patient profile for ID: {}", request.getPatientId());
        return PatientDetailResponse.from(patient, AccountInfo.from(account));
    }
}
