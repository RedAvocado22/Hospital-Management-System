package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.ValidationException;
import com.hospital.hms.pharmacy.dto.request.GetMedicineDetailRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GetMedicineDetailService extends BaseService<GetMedicineDetailRequest, MedicineResponse> {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponse doProcess(GetMedicineDetailRequest request) {
        Optional<Medicine> medicineOpt = medicineRepository.findById(request.getId());

        if (medicineOpt.isEmpty()) {
            log.warn("Medicine not found with id: {}", request.getId());
            throw new RuntimeException("Medicine not found");
        }

        Medicine medicine = medicineOpt.get();

        // Use mapper to dto response
        return medicineMapper.toResponse(medicine);
    }

    @Override
    public void validate(GetMedicineDetailRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("Medicine id must not be null");
        }
    }

}
