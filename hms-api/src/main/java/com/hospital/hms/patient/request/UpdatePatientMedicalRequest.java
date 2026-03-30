package com.hospital.hms.patient.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePatientMedicalRequest extends BaseRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private String bloodType;
    private String allergies;
}
