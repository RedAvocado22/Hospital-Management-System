package com.hospital.hms.medical.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.employee.service.EmployeeQueryService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import com.hospital.hms.medical.request.UpdateMedicalRecordRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMedicalRecordService extends BaseService<UpdateMedicalRecordRequest, MedicalRecordDetailResponse> {

    private final MedicalRecordRepository medicalRecordRepository;
    private final EmployeeQueryService employeeQueryService;

    @Override
    @Transactional
    public MedicalRecordDetailResponse execute(UpdateMedicalRecordRequest request) {
        return super.execute(request);
    }

    @Override
    protected MedicalRecordDetailResponse doProcess(UpdateMedicalRecordRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        MedicalRecord response = medicalRecordRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Medical record not found")
        );

        if (!response.getDoctor().getId().equals(request.getUserContext().getUserId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        if (request.getAdvice() != null && !request.getAdvice().isBlank()) {
            response.setDoctorAdvice(request.getAdvice());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            response.setDescription(request.getDescription());
        }

        MedicalRecord saved = medicalRecordRepository.save(response);

        EmployeeResponse er = employeeQueryService.getByAccountId(saved.getDoctor().getId());

        return MedicalRecordDetailResponse.from(saved, er);
    }
}
