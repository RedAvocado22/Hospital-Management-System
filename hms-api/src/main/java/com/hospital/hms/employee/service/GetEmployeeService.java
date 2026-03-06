package com.hospital.hms.employee.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.repository.EmployeeSpecification;
import com.hospital.hms.employee.request.SearchEmployeeRequest;
import com.hospital.hms.employee.response.EmployeeResponse;
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
public class GetEmployeeService extends BaseService<SearchEmployeeRequest, PaginatedResponse<EmployeeResponse>> {

    private final EmployeeInfoRepository employeeInfoRepository;

    @Override
    protected PaginatedResponse<EmployeeResponse> doProcess(SearchEmployeeRequest request) {
        log.debug(
                "Fetching employees with pagination - page: {}, size: {}",
                request.getPage(), request.getSize()
        );

        Pageable pageable = request.toPageable();
        Specification<EmployeeInfo> spec = EmployeeSpecification.withFilters(request);

        Page<EmployeeInfo> employees = employeeInfoRepository.findAll(spec, pageable);

        log.info("Found {} employees on page {} of {}",
                employees.getNumberOfElements(),
                employees.getNumber(),
                employees.getTotalPages()
        );

        Page<EmployeeResponse> employeeResponsePage = employees.map(EmployeeResponse::from);

        return PaginatedResponse.from(employeeResponsePage);
    }

    @Override
    protected void validate(SearchEmployeeRequest request) {
        super.validate(request);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<EmployeeResponse> execute(SearchEmployeeRequest request) {
        return super.execute(request);
    }
}
