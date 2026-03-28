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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
    @Operation(
            summary = "Search medical records",
            description = """
                    Returns a paginated list of medical records with optional filters.

                    **ABAC rule:** Doctors receive only records they created (doctorId is extracted from the JWT automatically). \
                    Admins and Receptionists see all records.

                    **Filters:** keyword (full-text on patient name/phone), doctorName (LIKE), date range (from/to).
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Medical records retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — ADMIN, RECEPTIONIST or DOCTOR role required"
            )
    })
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
    @Operation(
            summary = "Get medical record detail",
            description = """
                    Returns full detail of a single medical record including patient info, doctor summary, \
                    description, and advice.

                    **ABAC rule:** Doctors can only retrieve records they created. \
                    Admins and Receptionists can retrieve any record. \
                    A Doctor requesting another doctor's record will receive 403.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Medical record retrieved successfully",
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
                    description = "Medical record not found"
            )
    })
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> getMedicalRecordDetails(
            @Parameter(description = "UUID of the medical record to retrieve", required = true)
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
    @Operation(
            summary = "Create a medical record",
            description = """
                    Creates a new medical record for a patient after examination. \
                    Only accessible by Doctors.

                    **Side effects (all in one transaction):**
                    - Saves the MedicalRecord
                    - Auto-creates an empty ServiceInvoice linked to this record
                    - Auto-creates an empty MedicineInvoice linked to this record

                    If any of the above fail, the entire transaction rolls back — no partial data is saved.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Medical record created successfully with auto-generated invoices",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input — description or advice missing"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — DOCTOR role required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request
    ) {
        log.info("Creating medical record, doctor from JWT");

        MedicalRecordDetailResponse data = createMedicalRecordService.execute(request);

        log.info("Medical record created");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "Create medical record successfully", HttpStatus.CREATED.value())
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @Operation(
            summary = "Update a medical record",
            description = """
                    Updates the description and/or advice on an existing medical record. \
                    Only accessible by Doctors.

                    **ABAC rule:** A Doctor can only update records they created. \
                    Attempting to update another doctor's record returns 403.

                    Both fields are optional — only non-null values are applied.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Medical record updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied — DOCTOR role required or ABAC ownership check failed"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Medical record not found"
            )
    })
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponse>> updateMedicalRecord(
            @Parameter(description = "UUID of the medical record to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMedicalRecordRequest request
    ) {
        request.setId(id);

        log.info("Updating medical record id: {}", id);

        MedicalRecordDetailResponse data = updateMedicalRecordService.execute(request);

        log.info("Medical record {} updated", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Update medical record detail successfully", HttpStatus.OK.value())
        );
    }
}
