package com.hospital.hms.appointment.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookAppointmentRequest extends BaseRequest {

    @NotNull
    private UUID scheduleId;

    @NotBlank(message = "Please tell us about your reason")
    private String reason;

    private UUID patientId;
}
