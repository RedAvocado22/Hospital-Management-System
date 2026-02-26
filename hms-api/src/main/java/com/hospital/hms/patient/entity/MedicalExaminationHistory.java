package com.hospital.hms.patient.entity;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "medical_examination_history")
@AttributeOverride(name = "id", column = @Column(name = "examination_history_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicalExaminationHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfo patient;

    @Column(name = "examination_description", columnDefinition = "TEXT")
    private String examinationDescription;

    @Column(name = "examination_date")
    private LocalDate examinationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_counter_staff_id", nullable = false)
    private Account receptionCounterStaff;
}
