package com.hospital.hms.employee.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.GetEmployeeDetailRequest;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetEmployeeDetailService extends BaseService<GetEmployeeDetailRequest, EmployeeResponse> {

    private final EmployeeInfoRepository employeeInfoRepository;

    @Override
    protected EmployeeResponse doProcess(GetEmployeeDetailRequest request) {
        log.info("Fetching employee with id: {}", request.getId());

        EmployeeInfo employee = employeeInfoRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Employee with id: " + request.getId() + " not found")
        );

        log.info("Found employee with id: {}", employee.getId());
        return EmployeeResponse.from(employee);
    }

    @Override
    protected void validate(GetEmployeeDetailRequest request) {
        super.validate(request);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse execute(GetEmployeeDetailRequest request) {
        return super.execute(request);
    }
}
