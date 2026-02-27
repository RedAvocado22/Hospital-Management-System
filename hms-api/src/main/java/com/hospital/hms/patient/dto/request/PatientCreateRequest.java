package com.hospital.hms.patient.dto.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientCreateRequest extends BaseRequest {

    private UUID accountId;

    @NotBlank(message = "BloodType is required")
    @Size(min = 1, max = 5, message = "BloodType must be between 1 and 5 characters")
    private String bloodType;

    @NotNull(message = "Allergies status is required")
    private String allergies;
}
