package com.hospital.hms.pharmacy.dto.request;

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

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicineCreateRequest extends BaseRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @NotBlank(message = "Quantity is required")
    @Size(min = 1, max = 50, message = "Quantity must be between 1 and 50 characters")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 5000, message = "Description must be between 20 and 5000 characters")
    private String description;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Override
    public void validate() {
        super.validate();

        if (name != null && !name.matches(".*[a-zA-Z]{2,}.*")) {
            throw new IllegalArgumentException("Name must contain at least 2 alphabetic characters");
        }
        if (name != null && name.contains(" ")) {
            throw new IllegalArgumentException("Name contains excessive whitespace");
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
        if (isActive == null) {
            throw new IllegalArgumentException("Active status is required");
        }
        if (name != null && description != null && name.equals(description)) {
            throw new IllegalArgumentException("Name and description cannot be identical");
        }
        if (description != null && description.length() > 5000) {
            throw new IllegalArgumentException("Description is too long");
        }
    }
}
