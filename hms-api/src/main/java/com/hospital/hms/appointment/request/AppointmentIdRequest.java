package com.hospital.hms.appointment.request;

import com.hospital.hms.base.request.BaseRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppointmentIdRequest extends BaseRequest {
    private UUID id;
}
