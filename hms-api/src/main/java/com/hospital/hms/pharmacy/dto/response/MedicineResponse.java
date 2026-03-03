package com.hospital.hms.pharmacy.dto.response;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;


@Builder
public record MedicineResponse(UUID id,
                               String name,
                               String description,
                               BigDecimal price,
                               Integer quantity,
                               Boolean isActive) {

}
