package com.hospital.hms.pharmacy.dto.request;

import com.hospital.hms.base.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicineGetAllRequest extends PaginatedRequest {

    private UUID id;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    public void validate() {
    }
}
