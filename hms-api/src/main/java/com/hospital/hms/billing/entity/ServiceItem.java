package com.hospital.hms.billing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_item")
@IdClass(ServiceItemId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private HospitalService service;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_invoice_id", nullable = false)
    private ServiceInvoice serviceInvoice;

    @Column(name = "number_service_use", nullable = false)
    private Integer numberServiceUse;
}
