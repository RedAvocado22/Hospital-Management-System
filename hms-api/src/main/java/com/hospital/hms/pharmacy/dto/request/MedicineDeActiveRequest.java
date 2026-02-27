package com.hospital.hms.pharmacy.dto.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
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
public class MedicineDeActiveRequest extends BaseRequest {

    @NotBlank(message = "Medicine id are required ")
    private UUID id;
}
