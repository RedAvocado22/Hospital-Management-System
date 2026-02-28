package com.hospital.hms.billing.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemId implements Serializable {

    private UUID service;

    private UUID serviceInvoice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceItemId that = (ServiceItemId) o;
        return Objects.equals(service, that.service)
                && Objects.equals(serviceInvoice, that.serviceInvoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, serviceInvoice);
    }
}
