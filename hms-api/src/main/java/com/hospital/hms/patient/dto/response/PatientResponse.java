package com.hospital.hms.patient.dto.response;


import com.hospital.hms.base.response.BaseResponse;
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
public class PatientResponse extends BaseResponse {

    private UUID id;

    private UUID accountId;

    private String bloodType;

    private String allergies;

}
