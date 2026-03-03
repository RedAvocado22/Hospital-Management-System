package com.hospital.hms.patient.dto.response;


import lombok.Builder;

import java.util.UUID;

@Builder
public record PatientResponse(UUID id,
                              UUID accountId,
                              String bloodType,
                              String allergies) {

}
