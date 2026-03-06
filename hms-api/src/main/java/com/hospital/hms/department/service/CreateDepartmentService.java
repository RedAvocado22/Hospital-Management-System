package com.hospital.hms.department.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.repository.DepartmentRepository;
import com.hospital.hms.department.request.CreateDepartmentRequest;
import com.hospital.hms.department.response.DepartmentResponse;
import com.hospital.hms.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateDepartmentService extends BaseService<CreateDepartmentRequest, DepartmentResponse> {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    protected DepartmentResponse doProcess(CreateDepartmentRequest request) {
        log.info("Starting department creation: {}", request.getName());
        Department department = Department.builder()
                .name(request.getName())
                .isActive(request.getIsActive())
                .build();

        Department saved = departmentRepository.save(department);

        log.info("Department {} created successfully", request.getName());
        return DepartmentResponse.from(saved);
    }

    @Override
    protected void validate(CreateDepartmentRequest request) {
        super.validate(request);

        if (departmentRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Department's name already registered");
        }
    }
}
