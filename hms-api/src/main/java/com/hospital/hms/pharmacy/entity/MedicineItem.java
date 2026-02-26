package com.hospital.hms.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine_item")
@IdClass(MedicineItemId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_prescription_id", nullable = false)
    private MedicinePrescription medicinePrescription;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
