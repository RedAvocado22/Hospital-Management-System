package com.hospital.hms.department.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.repository.DepartmentRepository;
import com.hospital.hms.department.repository.DepartmentSpecification;
import com.hospital.hms.department.request.SearchDepartmentRequest;
import com.hospital.hms.department.response.DepartmentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GetDepartmentService extends BaseService<SearchDepartmentRequest, PaginatedResponse<DepartmentResponse>> {

    private final DepartmentRepository departmentRepository;

    @Override
    protected PaginatedResponse<DepartmentResponse> doProcess(SearchDepartmentRequest request) {
        log.debug(
                "Fetching departments with pagination - page: {}, size: {}, name: {}, active: {}, created at: {}, updated at: {}",
                request.getPage(), request.getSize(),
                request.getName(), request.getIsActive(), request.getCreatedAt(), request.getUpdatedAt()
        );
        Pageable pageable = request.toPageable();
        Specification<Department> spec = DepartmentSpecification.withFilters(request);

        Page<Department> departments = departmentRepository.findAll(spec, pageable);

        log.info("Found {} departments on page {} of {}",
                departments.getNumberOfElements(),
                departments.getNumber(),
                departments.getTotalPages()
        );

        Page<DepartmentResponse> departmentResponsePage = departments.map(DepartmentResponse::from);

        return PaginatedResponse.from(departmentResponsePage);
    }
}
