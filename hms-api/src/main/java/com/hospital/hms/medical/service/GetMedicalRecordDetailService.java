package com.hospital.hms.medical.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.dto.EmployeeSummary;
import com.hospital.hms.employee.service.EmployeeQueryService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import com.hospital.hms.medical.request.MedicalRecordIdRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import com.hospital.hms.patient.dto.PatientSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMedicalRecordDetailService extends BaseService<MedicalRecordIdRequest, MedicalRecordDetailResponse> {

    private final MedicalRecordRepository medicalRecordRepository;
    private final EmployeeQueryService employeeQueryService;

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordDetailResponse execute(MedicalRecordIdRequest request) {
        return super.execute(request);
    }

    @Override
    protected MedicalRecordDetailResponse doProcess(MedicalRecordIdRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        log.debug("Fetching medical record id: {} for user: {}",
                request.getId(), request.getUserContext().getUserId());

        MedicalRecord mr = medicalRecordRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Medical record with id: " + request.getId() + " not found")
        );

        if (request.getUserContext().hasRole("ROLE_PATIENT") &&
                !request.getUserContext().getUserId().equals(mr.getPatient().getAccount().getId())
        ) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        if (
                request.getUserContext().hasRole("DOCTOR")
                        && !request.getUserContext().getUserId().equals(mr.getDoctor().getId())
        ) {
            log.warn("Access denied: doctor {} attempted to view record {}",
                    request.getUserContext().getUserId(), request.getId());
            throw new AccessDeniedException("You are not allowed to view this medical record");
        }

        EmployeeSummary employeeInfo = employeeQueryService.getInfoByAccountId(mr.getDoctor().getId());
        PatientSummary patientSummary = PatientSummary.from(mr.getPatient());

        return MedicalRecordDetailResponse.from(mr, employeeInfo, patientSummary);
    }
}
