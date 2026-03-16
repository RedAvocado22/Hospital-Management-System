package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.exception.ValidationException;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import com.hospital.hms.pharmacy.request.UpdateMedicineRequest;
import com.hospital.hms.pharmacy.response.MedicineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMedicineService extends BaseService<UpdateMedicineRequest, MedicineResponse> {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    @Transactional
    protected MedicineResponse doProcess(UpdateMedicineRequest request) {
        log.debug("Processing medicine updating request: {}", request.getName());

        Medicine medicine = medicineRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Medicine", request.getId())
        );

        medicine.setDescription(request.getDescription());
        medicine.setName(request.getName());
        medicine.setPrice(request.getPrice());
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        log.info("Medicine saved with ID: {}",
                updatedMedicine.getId());

        MedicineResponse response = medicineMapper.toResponse(updatedMedicine);

        return response;

    }

    @Override
    public void validate(UpdateMedicineRequest request) {
        super.validate(request);
        log.debug("Starting service-level validation for medicine update: {}", request.getName());

        log.info("Service-level validation passed for medicine update: {}", request.getName());

        if (request.getDescription().length() < 200) {
            throw new ValidationException(
                    "Medicine require very detailed descriptions (minimum 200 characters)"
            );
        }
    }
}
