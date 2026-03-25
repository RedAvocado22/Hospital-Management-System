package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.medical.request.CreateMedicalRecordRequest;
import com.hospital.hms.medical.request.MedicalRecordIdRequest;
import com.hospital.hms.medical.request.SearchMedicalRecordRequest;
import com.hospital.hms.medical.request.UpdateMedicalRecordRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import com.hospital.hms.medical.response.MedicalRecordResponse;
import com.hospital.hms.medical.service.CreateMedicalRecordService;
import com.hospital.hms.medical.service.GetMedicalRecordDetailService;
import com.hospital.hms.medical.service.GetMedicalRecordService;
import com.hospital.hms.medical.service.UpdateMedicalRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records Management", description = "Endpoints for managing medical records")
@SecurityRequirement(name = "bearerAuth")
public class MedicalRecordController {

    private final GetMedicalRecordService getMedicalRecordService;
    private final GetMedicalRecordDetailService getMedicalRecordDetailService;
    private final CreateMedicalRecordService createMedicalRecordService;
    private final UpdateMedicalRecordService updateMedicalRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PaginatedResponse<MedicalRecordResponse>>> getMedicalRecords(
            @Valid @ModelAttribute SearchMedicalRecordRequest request
    ) {
        PaginatedResponse<MedicalRecordResponse> data = getMedicalRecordService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get medical records successfully", HttpStatus.OK.value())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> getMedicalRecordDetails(
            @PathVariable UUID id
    ) {
        MedicalRecordIdRequest request = new MedicalRecordIdRequest(id);

        MedicalRecordDetailResponse data = getMedicalRecordDetailService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get medical record detail successfully", HttpStatus.OK.value())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request
    ) {
        MedicalRecordDetailResponse data = createMedicalRecordService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "Create medical record successfully", HttpStatus.CREATED.value())
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> updateMedicalRecord(
            @Valid @RequestBody UpdateMedicalRecordRequest request,
            @PathVariable UUID id
    ) {
        request.setId(id);

        MedicalRecordDetailResponse data = updateMedicalRecordService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Update medical record detail successfully", HttpStatus.OK.value())
        );
    }
}
