package com.hospital.hms.billing.entity;

import com.hospital.hms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "service")
@AttributeOverride(name = "id", column = @Column(name = "service_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HospitalService extends BaseEntity {

    @Column(name = "service_name", length = 100, nullable = false)
    private String serviceName;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;
}
