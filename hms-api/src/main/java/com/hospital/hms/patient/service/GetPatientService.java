package com.hospital.hms.patient.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.repository.PatientInfoRepository;
import com.hospital.hms.patient.repository.PatientSpecification;
import com.hospital.hms.patient.request.SearchPatientRequest;
import com.hospital.hms.patient.response.PatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPatientService extends BaseService<SearchPatientRequest, PaginatedResponse<PatientResponse>> {

    private final PatientInfoRepository patientInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<PatientResponse> execute(SearchPatientRequest request) {
        return super.execute(request);
    }

    @Override
    protected PaginatedResponse<PatientResponse> doProcess(SearchPatientRequest request) {
        log.debug(
                "Fetching patients with pagination - page: {}, size: {}",
                request.getPage(), request.getSize()
        );

        Pageable pageable = request.toPageable();
        Specification<PatientInfo> spec = PatientSpecification.withFilters(request);

        Page<PatientInfo> patients = patientInfoRepository.findAll(spec, pageable);

        log.info("Found {} patients on page {} of {}",
                patients.getNumberOfElements(),
                patients.getNumber(),
                patients.getTotalPages()
        );

        Page<PatientResponse> responsePage = patients.map(patient ->
                PatientResponse.from(patient, AccountInfo.from(patient.getAccount()))
        );

        return PaginatedResponse.from(responsePage);
    }
}

