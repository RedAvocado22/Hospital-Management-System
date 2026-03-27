package com.hospital.hms.appointment.controller;

import com.hospital.hms.appointment.request.BookAppointmentRequest;
import com.hospital.hms.appointment.response.BookAppointmentResponse;
import com.hospital.hms.appointment.service.BookAppointmentService;
import com.hospital.hms.base.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final BookAppointmentService bookAppointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BookAppointmentResponse>> bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request
    ) {
        log.info("Booking appointment — doctorId: {}, date: {}",
                request.getDoctorId(), request.getDate());

        BookAppointmentResponse data = bookAppointmentService.execute(request);

        log.info("Appointment booked successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, "Booked successfully", HttpStatus.CREATED.value())
        );
    }
}
