package com.hospital.hms.pharmacy.dto.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
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
public class GetMedicineDetailRequest extends BaseRequest {

    @NotNull(message = "Medicine id are required ")
    private UUID id;
}
