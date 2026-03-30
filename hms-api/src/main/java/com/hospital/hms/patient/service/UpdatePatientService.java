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

        // 1. Load patient + account
        PatientInfo patient = patientInfoRepository.findWithAccountById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + request.getPatientId()));

        Account account = patient.getAccount();
        String accountIdStr = account.getId().toString();

        // 2. ABAC check
        if (request.getUserContext() == null || !request.getUserContext().getUserId().equals(account.getId())) {
            log.warn("Access denied: User {} attempted to update patient profile {}", 
                request.getUserContext() != null ? request.getUserContext().getUserId() : "anonymous", account.getId());
            throw new AccessDeniedException("You are not allowed to update this patient profile");
        }

        // 3. Lưu old values: oldEmail, oldFirstName, oldLastName
        String oldEmail = account.getEmail();
        String oldFirstName = account.getFirstName();
        String oldLastName = account.getLastName();

        // 4. Set new values lên account (partial update)
        if (request.getEmail() != null) account.setEmail(request.getEmail());
        if (request.getFirstName() != null) account.setFirstName(request.getFirstName());
        if (request.getLastName() != null) account.setLastName(request.getLastName());
        if (request.getDob() != null) account.setDob(request.getDob());
        if (request.getGender() != null) account.setGender(request.getGender());
        if (request.getAddress() != null) account.setAddress(request.getAddress());
        if (request.getPhone() != null) account.setPhone(request.getPhone());

        // 5. Call Keycloak
        // 6. Nếu Keycloak fail → catch → throw IdentityProviderException (handled by KeycloakService)
        keycloakService.updateUserInfo(accountIdStr, account.getEmail(), account.getFirstName(), account.getLastName());

        try {
            // 7. Save account to MySQL (dirty checking sẽ flush)
            patientInfoRepository.saveAndFlush(patient);
        } catch (Exception e) {
            // 8. Nếu MySQL fail → catch → call keycloakService.updateUserInfo(...) để revert → rethrow
            log.error("Failed to save account to MySQL, reverting Keycloak update for user: {}", accountIdStr, e);
            try {
                keycloakService.updateUserInfo(accountIdStr, oldEmail, oldFirstName, oldLastName);
            } catch (Exception revertEx) {
                log.error("CRITICAL: Failed to revert Keycloak update for user {}", accountIdStr, revertEx);
            }
            throw e;
        }

        // 9. Nếu password không null → call keycloakService.updatePassword(...)
        if (request.getPassword() != null) {
            log.info("Updating password in Keycloak for user ID: {}", accountIdStr);
            keycloakService.updatePassword(accountIdStr, request.getPassword());
        }

        // 10. Return PatientDetailResponse
        log.info("Successfully updated patient profile for ID: {}", request.getPatientId());
        return PatientDetailResponse.from(patient, AccountInfo.from(account));
    }
}

