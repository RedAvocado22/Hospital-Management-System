package com.hospital.hms.exam_queue.entity;

import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.patient.entity.PatientInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "queue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QueueInfo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfo patient;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QueueStatus status;

}
