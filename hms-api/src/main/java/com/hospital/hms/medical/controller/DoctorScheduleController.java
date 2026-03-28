package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.medical.request.SearchDoctorScheduleRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import com.hospital.hms.medical.service.SearchDoctorScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/doctor-schedules")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
public class DoctorScheduleController {
    private final SearchDoctorScheduleService searchDoctorScheduleService;

    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorScheduleDetailResponse>>> getDoctorSchedule(@ModelAttribute SearchDoctorScheduleRequest request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Get Doctor Schedule with doctor id : {}", traceId, request.getDoctorId());

        PaginatedResponse<DoctorScheduleDetailResponse> response = searchDoctorScheduleService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<PaginatedResponse<DoctorScheduleDetailResponse>> apiResponse = ApiResponse.success(
                response,
                "Get Doctor Schedule successfully",
                HttpStatus.OK.value(),
                metadata
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
