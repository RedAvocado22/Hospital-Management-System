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
@RequestMapping("/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final GetMedicineDetailService getMedicineDetailService;
    private final GetAllMedicineService getAllMedicineService;
    private final CreateMedicineService createMedicineService;
    private final DeActiveMedicineService deActiveMedicineService;
    private final UpdateMedicineService updateMedicineService;

    @PatchMapping("/deactivate")
    public ResponseEntity<ApiResponse<Void>> deActiveMedicine(@Valid @RequestBody DeActiveMedicineRequest request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] De-active medicine with medicine id : {}", traceId, request.getId());

        deActiveMedicineService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<Void> response = ApiResponse.success(
                null,
                "De-active medicine successfully",
                HttpStatus.OK.value(),
                metadata
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(@Valid @RequestBody CreateMedicineRequest request, HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Creating new medicine with name: {}", traceId, request.getName());

        MedicineResponse response = createMedicineService.execute(request);

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
                "Medicine created successfully",
                HttpStatus.CREATED.value(),
                metadata
        );

        log.info("[TraceID: {}] Medicine created successfully with ID: {} (took {}ms)",
                traceId, response.id(), duration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<MedicineResponse>>> getAllMedicines(@Valid @ModelAttribute GetAllMedicineRequest request, HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Fetching all medicines{}", traceId, request.getId() != null);

        PaginatedResponse<MedicineResponse> medicines = getAllMedicineService.execute(request);

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
                traceId, medicines.size(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicine(@RequestBody @Valid GetMedicineDetailRequest request, HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Fetching medicine with Medicine Id: {}", traceId, request.getId());

        MedicineResponse response = getMedicineDetailService.execute(request);
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
                traceId, response.name(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(@Valid @RequestBody UpdateMedicineRequest request, HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Updating medicine with id: {}", traceId, request.getId());

        MedicineResponse response = updateMedicineService.execute(request);

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
                traceId, response.name(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);

    }
}
