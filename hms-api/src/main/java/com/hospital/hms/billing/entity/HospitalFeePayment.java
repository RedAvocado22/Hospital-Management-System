package com.hospital.hms.billing.entity;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.patient.entity.PatientInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hospital_fee_payment")
@AttributeOverride(name = "id", column = @Column(name = "fee_payment_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HospitalFeePayment extends BaseEntity {

    @Column(name = "total_fee", precision = 10, scale = 2)
    private BigDecimal totalFee;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_paid", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfo patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_counter_staff_id", nullable = false)
    private Account cashierCounterStaff;
}
