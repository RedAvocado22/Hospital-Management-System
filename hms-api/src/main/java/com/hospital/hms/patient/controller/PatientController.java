package com.hospital.hms.patient.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.patient.request.PatientIdRequest;
import com.hospital.hms.patient.request.SearchPatientRequest;
import com.hospital.hms.patient.request.UpdatePatientMedicalRequest;
import com.hospital.hms.patient.request.UpdatePatientRequest;
import com.hospital.hms.patient.response.PatientDetailResponse;
import com.hospital.hms.patient.response.PatientResponse;
import com.hospital.hms.patient.service.GetPatientDetailService;
import com.hospital.hms.patient.service.GetPatientService;
import com.hospital.hms.patient.service.UpdatePatientMedicalService;
import com.hospital.hms.patient.service.UpdatePatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final GetPatientService getPatientService;
    private final GetPatientDetailService getPatientDetailService;
    private final UpdatePatientService updatePatientService;
    private final UpdatePatientMedicalService updatePatientMedicalService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @Operation(summary = "Search patients")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ApiResponse<PaginatedResponse<PatientResponse>>> getPatients(
            @Valid @ModelAttribute SearchPatientRequest request
    ) {
        log.info("REST request to search patients");
        PaginatedResponse<PatientResponse> data = getPatientService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get patients successfully", HttpStatus.OK.value())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @Operation(summary = "Get patient detail")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ApiResponse<PatientDetailResponse>> getPatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id
    ) {
        log.info("REST request to get patient detail: {}", id);
        PatientIdRequest request = new PatientIdRequest(id);
        PatientDetailResponse data = getPatientDetailService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get patient successfully", HttpStatus.OK.value())
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Update patient profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ApiResponse<PatientDetailResponse>> updatePatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientRequest request
    ) {
        log.info("REST request to update patient profile: {}", id);
        request.setPatientId(id);
        PatientDetailResponse data = updatePatientService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Update patient successfully", HttpStatus.OK.value())
        );
    }

    @PutMapping("/{id}/medical")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Update patient medical profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ApiResponse<PatientDetailResponse>> updatePatientMedical(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientMedicalRequest request
    ) {
        log.info("REST request to update patient medical profile: {}", id);
        request.setPatientId(id);
        PatientDetailResponse data = updatePatientMedicalService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Update patient medical profile successfully", HttpStatus.OK.value())
        );
    }
}
