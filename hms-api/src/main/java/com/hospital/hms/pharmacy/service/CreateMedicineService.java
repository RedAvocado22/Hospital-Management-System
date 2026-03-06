package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.ValidationException;
import com.hospital.hms.pharmacy.dto.request.CreateMedicineRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMedicineService extends BaseService<CreateMedicineRequest, MedicineResponse> {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    @Transactional
    public MedicineResponse doProcess(CreateMedicineRequest request) {
        log.debug("Processing medicine creation request: {}", request.getName());

        Medicine medicine = medicineMapper.toEntity(request);

        medicineRepository.save(medicine);

        return null;
    }

    @Override
    public void validate(CreateMedicineRequest request) {
        super.validate(request);

        log.debug("Starting service-level validation for medicine : {}", request.getName());

        boolean nameExists = medicineRepository.findByName(request.getName()).isPresent();

        if (nameExists) {
            throw new BusinessException(
                    "A medicine with the name '" + request.getName() + "' already exists. Please use a unique name."
            );
        }

        if (request.getDescription().length() < 200) {
            throw new ValidationException(
                    "Medicine require very detailed descriptions (minimum 200 characters)"
            );
        }
    }

}
