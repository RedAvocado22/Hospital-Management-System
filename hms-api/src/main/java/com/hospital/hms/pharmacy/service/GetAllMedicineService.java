package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import com.hospital.hms.pharmacy.repository.MedicineSpecification;
import com.hospital.hms.pharmacy.request.GetAllMedicineRequest;
import com.hospital.hms.pharmacy.response.MedicineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllMedicineService extends BaseService<GetAllMedicineRequest, PaginatedResponse<MedicineResponse>> {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    @Transactional(readOnly = true)
    protected PaginatedResponse<MedicineResponse> doProcess(GetAllMedicineRequest request) {
        log.debug("Fetching medicine with pagination - page: {}, size: {}, name: {}, quantity: {}, price: {}, description: {}", request.getPage(), request.getSize(), request.getName(), request.getQuantity(), request.getPrice(), request.getDescription());

        Pageable pageable = request.toPageable();
        Specification<Medicine> spec = MedicineSpecification.withFilters(request);

        Page<Medicine> medicinePage = medicineRepository.findAll(spec, pageable);

        if (medicinePage.isEmpty()) {
            log.debug("No medicines found for given filters");
        }

        log.info("Found {} medicine on page {} of {}", medicinePage.getNumberOfElements(), medicinePage.getNumber(), medicinePage.getTotalPages());

        Page<MedicineResponse> responsePage = medicinePage.map(medicineMapper::toResponse);
        return PaginatedResponse.from(responsePage);
    }

}
