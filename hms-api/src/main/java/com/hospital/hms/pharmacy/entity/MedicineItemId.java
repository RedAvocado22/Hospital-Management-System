package com.hospital.hms.pharmacy.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineItemId implements Serializable {

    private Integer medicinePrescription;

    private Integer medicine;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineItemId that = (MedicineItemId) o;
        return Objects.equals(medicinePrescription, that.medicinePrescription)
                && Objects.equals(medicine, that.medicine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicinePrescription, medicine);
    }
}
