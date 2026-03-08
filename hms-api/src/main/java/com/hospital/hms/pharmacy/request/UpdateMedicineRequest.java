package com.hospital.hms.pharmacy.request;

import com.hospital.hms.base.request.BaseRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateMedicineRequest extends BaseRequest {

    @NotNull(message = "Medicine id are required ")
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    private Integer quantity;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(min = 20, max = 5000, message = "Description must be between 20 and 5000 characters")
    private String description;

    @Override
    public void validate() {
        super.validate();

        if (name != null && !name.matches(".*[a-zA-Z]{2,}.*")) {
            throw new IllegalArgumentException("Name must contain at least 2 alphabetic characters");
        }
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (description != null && description.trim().length() < 20) {
            throw new IllegalArgumentException("Description must have at least 20 non-whitespace characters");
        }
        if (name != null && name.equals(description)) {
            throw new IllegalArgumentException("Name and description cannot be identical");
        }
        if (description != null && description.length() > 5000) {
            throw new IllegalArgumentException("Description is too long");
        }
    }

}
