package com.hospital.hms.medical.entity;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.common.enums.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "doctor_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DoctorSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Account doctor;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "shift_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftType type;

    @Column(name = "max_patients", nullable = false)
    private Integer maxPatients;

    @Column(name = "is_available", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isAvailable;
}
