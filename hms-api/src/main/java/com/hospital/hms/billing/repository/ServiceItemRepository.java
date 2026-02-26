package com.hospital.hms.billing.repository;

import com.hospital.hms.billing.entity.ServiceItem;
import com.hospital.hms.billing.entity.ServiceItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, ServiceItemId> {

    List<ServiceItem> findByServiceInvoice_Id(UUID serviceInvoiceId);
}
