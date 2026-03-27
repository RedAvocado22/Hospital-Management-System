package com.hospital.hms.appointment.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookAppointmentRequest extends BaseRequest {

    @NotNull(message = "Doctor is missing")
    private UUID doctorId;

    @NotNull(message = "The date can't be null")
    private LocalDate date;

    @NotNull(message = "The start time can't be null")
    private LocalTime startTime;

    @NotNull(message = "The end time can't be null")
    private LocalTime endTime;

    @NotBlank(message = "Please tell us about your reason")
    private String reason;

    private UUID patientId;
}
