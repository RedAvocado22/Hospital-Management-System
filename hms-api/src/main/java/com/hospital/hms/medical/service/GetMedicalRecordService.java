package com.hospital.hms.medical.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.medical.repository.MedicalRecordRepository;
import com.hospital.hms.medical.request.SearchMedicalRecordRequest;
import com.hospital.hms.medical.response.MedicalRecordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMedicalRecordService extends BaseService<SearchMedicalRecordRequest, PaginatedResponse<MedicalRecordResponse>> {

    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<MedicalRecordResponse> execute(SearchMedicalRecordRequest request) {
        return super.execute(request);
    }

    @Override
    protected PaginatedResponse<MedicalRecordResponse> doProcess(SearchMedicalRecordRequest request) {
        Pageable pageable = request.toPageable();

        String username = request.getUserContext() != null && request.getUserContext().hasRole("ROLE_DOCTOR") ? request.getUserContext().getUsername() : null;

        log.debug("Searching medical records — keyword: {}, doctorName: {}, doctorFilter: {}",
                request.getKeyword(), request.getDoctorName(), username);

        Page<MedicalRecordResponse> responses = medicalRecordRepository.getMedicalRecordBy(
                request.getKeyword() != null && request.getKeyword().isBlank() ? null : request.getKeyword(),
                request.getDoctorName(),
                request.getFrom() != null ? request.getFrom().atStartOfDay() : null,
                request.getTo() != null ? request.getTo().plusDays(1).atStartOfDay() : null,
                username,
                pageable
        );

        return PaginatedResponse.from(responses);
    }
}
