package com.hospital.hms.medical.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.medical.request.CreateDoctorScheduleRequest;
import com.hospital.hms.medical.request.DoctorScheduleIdRequest;
import com.hospital.hms.medical.request.SearchDoctorScheduleRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import com.hospital.hms.medical.service.CreateDoctorScheduleService;
import com.hospital.hms.medical.service.DeactivateDoctorScheduleService;
import com.hospital.hms.medical.service.SearchDoctorScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/doctor-schedules")
@RequiredArgsConstructor
@Tag(name = "Doctor Schedules", description = "Manage doctor shift schedules")
@SecurityRequirement(name = "bearerAuth")
public class DoctorScheduleController {

    private final SearchDoctorScheduleService searchDoctorScheduleService;
    private final CreateDoctorScheduleService createDoctorScheduleService;
    private final DeactivateDoctorScheduleService deactivateDoctorScheduleService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorScheduleDetailResponse>>> getDoctorSchedule(
            @ModelAttribute SearchDoctorScheduleRequest request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString();

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

    @Operation(
            summary = "Create a doctor schedule",
            description = """
                    Creates a shift schedule for a doctor. Shifts are fixed: MORNING (07:00–12:00), \
                    AFTERNOON (13:00–18:00), EVENING (19:00–22:00).
                    
                    Validates that the account has DOCTOR role and no overlapping shift exists for the same doctor and date.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Schedule created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Account is not a doctor or shift already exists for this date"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor account not found")
    })
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

    @Operation(
            summary = "Deactivate a doctor schedule",
            description = """
                    Sets a doctor's shift to unavailable. Blocked if:
                    - Schedule already deactivated
                    - Active (PENDING or CONFIRMED) appointments exist on this shift
                    - Shift starts within 24 hours
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Schedule deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cannot deactivate — active appointments exist or within 24h notice"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Schedule not found")
    })
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
