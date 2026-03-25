package com.hospital.hms.medical.service;

import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.billing.service.InvoiceQueryService;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.employee.service.EmployeeQueryService;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import com.hospital.hms.medical.request.CreateMedicalRecordRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import com.hospital.hms.patient.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMedicalRecordService extends BaseService<CreateMedicalRecordRequest, MedicalRecordDetailResponse> {

    private final PatientQueryService patientQueryService;
    private final EmployeeQueryService employeeQueryService;
    private final AccountQueryService accountQueryService;
    private final MedicalRecordRepository medicalRecordRepository;
    private final InvoiceQueryService invoiceQueryService;

    @Override
    @Transactional
    public MedicalRecordDetailResponse execute(CreateMedicalRecordRequest request) {
        return super.execute(request);
    }

    @Override
    protected MedicalRecordDetailResponse doProcess(CreateMedicalRecordRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        MedicalRecord mr = MedicalRecord.builder()
                .patient(patientQueryService.getReferenceById(request.getPatientId()))
                .doctor(accountQueryService.getReferenceById(request.getUserContext().getUserId()))
                .doctorAdvice(request.getAdvice())
                .description(request.getDescription())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(mr);

        invoiceQueryService.initInvoices(saved.getId());

        EmployeeResponse er = employeeQueryService.getByAccountId(request.getUserContext().getUserId());

        return MedicalRecordDetailResponse.from(saved, er);
    }
}
