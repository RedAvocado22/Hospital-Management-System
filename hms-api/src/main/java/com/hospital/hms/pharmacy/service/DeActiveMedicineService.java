package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import com.hospital.hms.pharmacy.request.DeActiveMedicineRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeActiveMedicineService extends BaseService<DeActiveMedicineRequest, Void> {

    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    protected Void doProcess(DeActiveMedicineRequest request) {
        log.debug("Processing medicine de-active request: {}", request.getId());


        Medicine medicine = medicineRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("medicine", request.getId())
        );

        if (!medicine.getIsActive()) {
            throw new BusinessException("Medicine with ID " + request.getId() + " is already deactivated");
        }

        medicine.setIsActive(false);
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine deActiveMedicine = medicineRepository.save(medicine);

        log.info("Medicine de-active with ID: {}",
                deActiveMedicine.getId());

        return null;
    }

}
