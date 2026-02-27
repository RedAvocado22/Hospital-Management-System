package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.pharmacy.dto.request.MedicineDeActiveRequest;
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
public class MedicineDeActiveService implements BaseService<MedicineDeActiveRequest, MedicineResponse> {

    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public MedicineResponse doProcess(MedicineDeActiveRequest request) {
        log.debug("Processing medicine de-active request: {}", request.getId());


        Medicine medicine = medicineRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("medicine", request.getId())
        );

        medicine.setIsActive(false);
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine deActiveMedicine = medicineRepository.save(medicine);

        log.info("medicine de-active with ID: {}",
                deActiveMedicine.getId());

        MedicineResponse response = MedicineMapper.INSTANCE.toResponse(deActiveMedicine);
        response.setProcessedAt(LocalDateTime.now());

        return response;
    }

    @Override
    public void validate(MedicineDeActiveRequest request) {
        BaseService.super.validate(request);
    }


    @Override
    public MedicineResponse execute(MedicineDeActiveRequest request) {
        return BaseService.super.execute(request);
    }

    @Override
    public Optional<MedicineResponse> executeOptional(MedicineDeActiveRequest request) {
        return BaseService.super.executeOptional(request);
    }

    @Override
    public Optional<MedicineResponse> executeSilent(MedicineDeActiveRequest request) {
        return BaseService.super.executeSilent(request);
    }
}
