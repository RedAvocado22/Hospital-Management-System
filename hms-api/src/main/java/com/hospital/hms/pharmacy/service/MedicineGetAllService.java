package com.hospital.hms.pharmacy.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.pharmacy.dto.request.MedicineGetAllRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.mapper.MedicineMapper;
import com.hospital.hms.pharmacy.repository.MedicineRepository;
import com.hospital.hms.pharmacy.repository.MedicineSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineGetAllService implements BaseService<MedicineGetAllRequest, PaginatedResponse<MedicineResponse>> {

    private final MedicineRepository medicineRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<MedicineResponse> doProcess(MedicineGetAllRequest request) {
        log.debug(
                "Fetching medicine with pagination - page: {}, size: {}, name: {}, quantity: {}, price: {}, description: {}",
                request.getPage(), request.getSize(),
                request.getName(), request.getQuantity(), request.getPrice(), request.getDescription()
        );

        Pageable pageable = request.toPageable();
        Specification<Medicine> spec = MedicineSpecification.withFilters(request);

        Page<Medicine> medicinePage = medicineRepository.findAll(spec, pageable);

        log.info("Found {} medicine on page {} of {}",
                medicinePage.getNumberOfElements(),
                medicinePage.getNumber(),
                medicinePage.getTotalPages());

        Page<MedicineResponse> courseResponsePage = medicinePage.map(MedicineMapper.INSTANCE::toResponse);
        return PaginatedResponse.from(courseResponsePage);
    }

    @Override
    public void validate(MedicineGetAllRequest request) {
        BaseService.super.validate(request);
        log.debug("No additional validation required for fetching all medicines");
    }

    @Override
    public PaginatedResponse<MedicineResponse> execute(MedicineGetAllRequest request) {
        return BaseService.super.execute(request);
    }

    @Override
    public Optional<PaginatedResponse<MedicineResponse>> executeOptional(MedicineGetAllRequest request) {
        return BaseService.super.executeOptional(request);
    }

    @Override
    public Optional<PaginatedResponse<MedicineResponse>> executeSilent(MedicineGetAllRequest request) {
        return BaseService.super.executeSilent(request);
    }
}
