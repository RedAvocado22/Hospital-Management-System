package com.hospital.hms.patient.entity;

import com.hospital.hms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "health_insurance")
@AttributeOverride(name = "id", column = @Column(name = "insurance_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HealthInsurance extends BaseEntity {

    @Column(name = "insurance_name", length = 100, nullable = false)
    private String insuranceName;

    @Column(name = "is_valid", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isValid;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfo patient;
}
