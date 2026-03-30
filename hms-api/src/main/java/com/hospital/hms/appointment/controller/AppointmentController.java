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
public class AppointmentController {
    private final BookAppointmentService bookAppointmentService;
    private final CancelAppointmentService cancelAppointmentService;
    private final ConfirmAppointmentService confirmAppointmentService;
    private final GetAppointmentService getAppointmentService;

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
