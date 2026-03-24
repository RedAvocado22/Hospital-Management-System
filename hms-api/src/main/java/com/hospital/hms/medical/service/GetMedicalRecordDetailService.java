package com.hospital.hms.medical.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
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
    private final EmployeeInfoRepository employeeInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordDetailResponse execute(MedicalRecordIdRequest request) {
        return super.execute(request);
    }

    @Override
    protected MedicalRecordDetailResponse doProcess(MedicalRecordIdRequest request) {
        MedicalRecord md = medicalRecordRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Medical record with id: " + request.getId() + " not found")
        );

        EmployeeInfo ei = employeeInfoRepository.findByAccount_Id(md.getDoctor().getId()).orElseThrow(
                () -> new NotFoundException("Employee info with id: " + md.getDoctor().getId() + " not found")
        );

        if (
                request.getUserContext().hasRole("DOCTOR")
                        && !request.getUserContext().getUserId().equals(md.getDoctor().getId())
        ) {
            throw new AccessDeniedException("You are not allow to view this medical record");
        }


        return MedicalRecordDetailResponse.from(md, ei);
    }
}
