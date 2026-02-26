package com.hospital.hms.billing.entity;

import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.medical.entity.MedicalRecord;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicine_invoice")
@AttributeOverride(name = "id", column = @Column(name = "medicine_invoice_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicineInvoice extends BaseEntity {

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(name = "is_paid", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isPaid;
}
