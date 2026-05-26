package com.hospital.hms.appointment.controller;

import com.hospital.hms.appointment.request.AppointmentIdRequest;
import com.hospital.hms.appointment.request.BookAppointmentRequest;
import com.hospital.hms.appointment.request.SearchAppointmentRequest;
import com.hospital.hms.appointment.response.AppointmentResponse;
import com.hospital.hms.appointment.service.BookAppointmentService;
import com.hospital.hms.appointment.service.CancelAppointmentService;
import com.hospital.hms.appointment.service.ConfirmAppointmentService;
import com.hospital.hms.appointment.service.GetAppointmentService;
import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment booking, cancellation, and confirmation")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {
    private final BookAppointmentService bookAppointmentService;
    private final CancelAppointmentService cancelAppointmentService;
    private final ConfirmAppointmentService confirmAppointmentService;
    private final GetAppointmentService getAppointmentService;

    @Operation(
            summary = "List appointments",
            description = """
                    Returns a paginated list of appointments with optional filters.

                    **ABAC rule:** DOCTOR sees only their own appointments. PATIENT sees only their own. \
                    ADMIN and RECEPTIONIST see all.

                    **Filters:** status, doctorName, patientName, date range.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Appointments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponse>>> getAllAppointments(
            @ModelAttribute SearchAppointmentRequest request
    ) {
        log.info("Fetching appointments — status: {}, doctorName: {}, patientName: {}",
                request.getStatus(), request.getDoctorName(), request.getPatientName());

        PaginatedResponse<AppointmentResponse> data = getAppointmentService.execute(request);

        log.info("Fetched {} appointments", data.content().size());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Get appointments successfully", HttpStatus.OK.value())
        );
    }

    @Operation(
            summary = "Book an appointment",
            description = """
                    Books a slot on a doctor's schedule. Uses Redis atomic increment to prevent overbooking.

                    **ABAC rule:** PATIENT books for themselves (patientId resolved from JWT). \
                    RECEPTIONIST must provide patientId explicitly.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Appointment booked successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Schedule full or invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request
    ) {
        log.info("Booking appointment — doctor schedule with id: {}", request.getScheduleId());

        AppointmentResponse data = bookAppointmentService.execute(request);

        log.info("Appointment booked successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "Booked successfully", HttpStatus.CREATED.value())
        );
    }

    @Operation(
            summary = "Cancel an appointment",
            description = """
                    Cancels a PENDING or CONFIRMED appointment and decrements the Redis slot count.

                    **ABAC rule:** PATIENT can only cancel their own appointments.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Appointment already cancelled or completed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Patient trying to cancel another patient's appointment"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @PathVariable UUID id
    ) {
        log.info("Cancel appointment with id: {}", id);

        AppointmentIdRequest request = new AppointmentIdRequest(id);
        AppointmentResponse data = cancelAppointmentService.execute(request);

        log.info("Appointment cancelled successfully");
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Cancelled successfully", HttpStatus.OK.value())
        );
    }

    @Operation(
            summary = "Confirm an appointment",
            description = """
                    Confirms a PENDING appointment. Cannot confirm past appointments.

                    **ABAC rule:** DOCTOR can only confirm appointments assigned to them.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Appointment confirmed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Appointment not in PENDING status or date is in the past"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Doctor trying to confirm another doctor's appointment"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(
            @PathVariable UUID id
    ) {
        log.info("Confirm appointment with id: {}", id);

        AppointmentIdRequest request = new AppointmentIdRequest(id);
        AppointmentResponse data = confirmAppointmentService.execute(request);

        log.info("Appointment confirmed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(data, "Confirmed successfully", HttpStatus.OK.value())
        );
    }
}
