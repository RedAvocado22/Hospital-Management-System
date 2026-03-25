package com.hospital.hms.medical.request;

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
public class CreateMedicalRecordRequest extends BaseRequest {

    @NotNull(message = "Id are required")
    private UUID patientId;

    private String advice;

    @NotBlank(message = "Description is required")
    private String description;
}
