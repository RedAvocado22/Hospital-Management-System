package com.hospital.hms.billing.service;

import com.hospital.hms.billing.entity.MedicineInvoice;
import com.hospital.hms.billing.entity.ServiceInvoice;
import com.hospital.hms.billing.repository.MedicineInvoiceRepository;
import com.hospital.hms.billing.repository.ServiceInvoiceRepository;
import com.hospital.hms.medical.entity.MedicalRecord;
import com.hospital.hms.medical.service.MedicalRecordQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceQueryService {
    private final MedicineInvoiceRepository medicineInvoiceRepository;
    private final ServiceInvoiceRepository serviceInvoiceRepository;
    private final MedicalRecordQueryService medicalRecordQueryService;

    @Transactional
    public void initInvoices(UUID id) {
        MedicalRecord mr = medicalRecordQueryService.getReferenceById(id);

        MedicineInvoice mi = MedicineInvoice.builder()
                .medicalRecord(mr)
                .isPaid(false)
                .build();
        medicineInvoiceRepository.save(mi);

        ServiceInvoice si = ServiceInvoice.builder()
                .medicalRecord(mr)
                .build();
        serviceInvoiceRepository.save(si);
    }
}
