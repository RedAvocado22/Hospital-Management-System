package com.hospital.hms.patient.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.patient.request.PatientIdRequest;
import com.hospital.hms.patient.request.SearchPatientRequest;
import com.hospital.hms.patient.response.PatientDetailResponse;
import com.hospital.hms.patient.response.PatientResponse;
import com.hospital.hms.patient.service.GetPatientDetailService;
import com.hospital.hms.patient.service.GetPatientService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "Endpoints for searching and viewing patients")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final GetPatientService getPatientService;
    private final GetPatientDetailService getPatientDetailService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @Operation(
            summary = "Search patients",
            description = """
                    Returns a paginated list of patients.

                    **Filters:** fullName (LIKE on account.fullName), email (LIKE on account.email), phone (LIKE on account.phone).
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Patients retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — ADMIN or RECEPTIONIST role required"
            )
    })
    public ResponseEntity<ApiResponse<PaginatedResponse<PatientResponse>>> getPatients(
            @Valid @ModelAttribute SearchPatientRequest request
    ) {
        PaginatedResponse<PatientResponse> data = getPatientService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get patients successfully", HttpStatus.OK.value())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @Operation(
            summary = "Get patient detail",
            description = """
                    Returns detailed information about a single patient including account and medical profile fields.

                    **ABAC rule:** PATIENT role can only view their own profile (patientInfo.account.id must match JWT subject). \
                    ADMIN, RECEPTIONIST, and DOCTOR can view any patient.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Patient retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — insufficient role or ABAC ownership check failed"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    public ResponseEntity<ApiResponse<PatientDetailResponse>> getPatient(
            @Parameter(description = "UUID of the patient to retrieve", required = true)
            @PathVariable UUID id
    ) {
        PatientIdRequest request = new PatientIdRequest(id);

        PatientDetailResponse data = getPatientDetailService.execute(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get patient successfully", HttpStatus.OK.value())
        );
    }
}

