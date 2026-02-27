package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.pharmacy.dto.request.MedicineCreateRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineCreateService implements BaseService<MedicineCreateRequest, MedicineResponse> {

    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public MedicineResponse doProcess(MedicineCreateRequest request) {
        log.debug("Processing medicine creation request: {}", request.getName());

        Medicine course = Medicine.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();

        Medicine savedMedicine = medicineRepository.save(course);

        log.info("Course saved with ID: {}", savedMedicine.getId());

        MedicineResponse response = MedicineMapper.INSTANCE.toResponse(savedMedicine);
        response.setProcessedAt(LocalDateTime.now());

        return response;
    }

    @Override
    public void validate(MedicineCreateRequest request) {
        BaseService.super.validate(request);

        log.debug("Starting service-level validation for medicine : {}", request.getName());

        boolean nameExists = medicineRepository.findByName(request.getName()).isPresent();

        if (nameExists) {
            throw new IllegalArgumentException(
                    "A medicine with the name '" + request.getName() + "' already exists. Please use a unique name."
            );
        }

        if (request.getDescription().length() < 200) {
            throw new IllegalArgumentException(
                    "Medicine require very detailed descriptions (minimum 200 characters)"
            );
        }
    }

    @Override
    public MedicineResponse execute(MedicineCreateRequest request) {
        return BaseService.super.execute(request);
    }

    @Override
    public Optional<MedicineResponse> executeOptional(MedicineCreateRequest request) {
        return BaseService.super.executeOptional(request);
    }

    @Override
    public Optional<MedicineResponse> executeSilent(MedicineCreateRequest request) {
        return BaseService.super.executeSilent(request);
    }
}
