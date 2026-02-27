package com.hospital.hms.patient.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.patient.dto.request.PatientCreateRequest;
import com.hospital.hms.patient.dto.response.PatientResponse;
import com.hospital.hms.patient.service.PatientCreateService;
import com.hospital.hms.pharmacy.dto.request.MedicineCreateRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientCreateService patientCreateService;

    @PostMapping()
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody PatientCreateRequest request,
            HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Creating new patient with ID: {}", traceId, request.getAccountId());

        request.initialize();
        PatientResponse patientResponse = patientCreateService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<PatientResponse> response = ApiResponse.success(
                patientResponse,
                "Patient created successfully",
                HttpStatus.CREATED.value(),
                metadata
        );

        log.info("[TraceID: {}] Patient created successfully with ID: {} (took {}ms)",
                traceId, patientResponse.getId(), duration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
