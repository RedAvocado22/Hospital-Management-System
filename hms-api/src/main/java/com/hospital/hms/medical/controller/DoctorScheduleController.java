package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.medical.request.CreateDoctorScheduleRequest;
import com.hospital.hms.medical.request.DoctorScheduleIdRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import com.hospital.hms.medical.service.CreateDoctorScheduleService;
import com.hospital.hms.medical.service.DeactivateDoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final CreateDoctorScheduleService createDoctorScheduleService;
    private final DeactivateDoctorScheduleService deactivateDoctorScheduleService;

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

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateDoctorSchedule(
            @PathVariable UUID id
    ) {
        DoctorScheduleIdRequest request = new DoctorScheduleIdRequest();
        request.setId(id);

        deactivateDoctorScheduleService.execute(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null, "Schedule deactivated successfully", HttpStatus.OK.value())
        );
    }
}
