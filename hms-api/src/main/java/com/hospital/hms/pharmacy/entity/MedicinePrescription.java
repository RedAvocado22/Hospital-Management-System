package com.hospital.hms.pharmacy.entity;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.billing.entity.MedicineInvoice;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "medicine_prescription")
@AttributeOverride(name = "id", column = @Column(name = "medicine_prescription_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicinePrescription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_invoice_id", nullable = false)
    private MedicineInvoice medicineInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Account doctor;
}
