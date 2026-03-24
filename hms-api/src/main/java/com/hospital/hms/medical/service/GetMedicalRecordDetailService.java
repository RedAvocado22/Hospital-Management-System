package com.hospital.hms.medical.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.employee.service.EmployeeQueryService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import com.hospital.hms.medical.request.MedicalRecordIdRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        MedicalRecord md = medicalRecordRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Medical record with id: " + request.getId() + " not found")
        );

        if (
                request.getUserContext().hasRole("DOCTOR")
                        && !request.getUserContext().getUserId().equals(md.getDoctor().getId())
        ) {
            throw new AccessDeniedException("You are not allow to view this medical record");
        }

        EmployeeResponse employeeResponse = employeeQueryService.getByAccountId(md.getDoctor().getId());

        return MedicalRecordDetailResponse.from(md, employeeResponse);
    }
}
