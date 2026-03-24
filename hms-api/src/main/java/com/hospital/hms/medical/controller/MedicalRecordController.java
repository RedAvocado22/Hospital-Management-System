package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.medical.request.MedicalRecordIdRequest;
import com.hospital.hms.medical.request.SearchMedicalRecordRequest;
import com.hospital.hms.medical.response.MedicalRecordDetailResponse;
import com.hospital.hms.medical.response.MedicalRecordResponse;
import com.hospital.hms.medical.service.GetMedicalRecordDetailService;
import com.hospital.hms.medical.service.GetMedicalRecordService;
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
}
