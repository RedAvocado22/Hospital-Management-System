package com.hospital.hms.medical.request;

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
public class UpdateMedicalRecordRequest extends BaseRequest {
    private UUID id;

    private String advice;

    private String description;
}
