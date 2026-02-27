package com.hospital.hms.pharmacy.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.pharmacy.dto.request.*;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineGetDetailService medicineGetDetailService;
    private final MedicineGetAllService medicineGetAllService;
    private final MedicineCreateService medicineCreateService;
    private final MedicineDeActiveService medicineDeActiveService;
    private final MedicineUpdateService medicineUpdateService;

    @PatchMapping("/de-activate")
    public ResponseEntity<ApiResponse<MedicineResponse>> deActiveMedicine(
            @Valid @RequestBody MedicineDeActiveRequest request,
            HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] De-active medicine with medicine id : {}", traceId, request.getId());

        request.initialize();
        MedicineResponse medicineResponse = medicineDeActiveService.execute(request);
        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<MedicineResponse> response = ApiResponse.success(
                medicineResponse,
                "Medicine de-activate successfully",
                HttpStatus.OK.value(),
                metadata
        );

        log.info("[TraceID: {}] Medicine de-active successfully with ID: {} (took {}ms)",
                traceId, medicineResponse.getId(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
            @Valid @RequestBody MedicineCreateRequest request,
            HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Creating new medicine with name: {}", traceId, request.getName());

        request.initialize();
        MedicineResponse medicineResponse = medicineCreateService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<MedicineResponse> response = ApiResponse.success(
                medicineResponse,
                "Medicine created successfully",
                HttpStatus.CREATED.value(),
                metadata
        );

        log.info("[TraceID: {}] Medicine created successfully with ID: {} (took {}ms)",
                traceId, medicineResponse.getId(), duration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<MedicineResponse>>> getAllMedicines(
            @Valid @ModelAttribute MedicineGetAllRequest request,
            HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Fetching all medicines{}", traceId,
                request.getId() != null);

        PaginatedResponse<MedicineResponse> medicines = medicineGetAllService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<PaginatedResponse<MedicineResponse>> apiResponse = ApiResponse.success(
                medicines,
                "Medicine retrieved successfully",
                HttpStatus.OK.value(),
                metadata
        );

        log.info("[TraceID: {}] Retrieved {} medicine successfully (took {}ms)",
                traceId, medicines.getSize(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicine(
            @RequestBody MedicineGetDetailRequest request,
            HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Fetching medicine with Medicine Id: {}", traceId, request.getId());

        request.initialize();

        MedicineResponse response = medicineGetDetailService.execute(request);
        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();
        ApiResponse<MedicineResponse> apiResponse = ApiResponse.success(
                response,
                "medicine retrieved successfully",
                HttpStatus.OK.value(),
                metadata
        );

        log.info("[TraceID: {}] Retrieved medicine: {} successfully (took {}ms)",
                traceId, response.getName(), duration);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateCourse(
            @Valid @RequestBody MedicineUpdateRequest request,
            HttpServletRequest httpRequest
    ) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Updating medicine with id: {}", traceId, request.getId());

        MedicineResponse response = medicineUpdateService.doProcess(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<MedicineResponse> apiResponse = ApiResponse.success(
                response,
                "Medicine updated successfully",
                HttpStatus.OK.value(),
                metadata
        );


        log.info("[TraceID: {}] Updated medicine: {} successfully (took {}ms)",
                traceId, response.getName(), duration);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
