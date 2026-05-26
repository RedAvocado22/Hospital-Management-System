package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.medical.request.CreateDoctorScheduleRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import com.hospital.hms.medical.service.CreateDoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final CreateDoctorScheduleService createDoctorScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DoctorScheduleDetailResponse>> createDoctorSchedule(
            @RequestBody CreateDoctorScheduleRequest request
    ) {
        DoctorScheduleDetailResponse data = createDoctorScheduleService.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                data,
                                "Create doctor schedule successfully",
                                HttpStatus.CREATED.value()
                        )
                );
    }
}
