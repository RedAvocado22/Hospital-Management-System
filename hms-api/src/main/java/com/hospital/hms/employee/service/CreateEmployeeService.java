package com.hospital.hms.employee.service;

import com.hospital.hms.auth.dto.AccountInfo;
import com.hospital.hms.auth.request.AccountRegistrationRequest;
import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.auth.service.AccountRegistrationService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.dto.DepartmentInfo;
import com.hospital.hms.department.service.DepartmentQueryService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.CreateEmployeeRequest;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateEmployeeService extends BaseService<CreateEmployeeRequest, EmployeeResponse> {

    private final EmployeeInfoRepository employeeInfoRepository;
    private final DepartmentQueryService departmentQueryService;
    private final AccountQueryService accountQueryService;
    private final AccountRegistrationService accountRegistrationService;

    @Override
    protected EmployeeResponse doProcess(CreateEmployeeRequest request) {
        log.info("Starting employee creation for username: {}", request.getUsername());

        AccountRegistrationRequest accountRegistrationRequest = AccountRegistrationRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        AccountInfo accountInfo = accountRegistrationService.execute(accountRegistrationRequest);
        DepartmentInfo departmentInfo = departmentQueryService.getDepartmentIdByName(request.getDepartment());

        UUID accountId = accountInfo.id();

        EmployeeInfo info = EmployeeInfo.builder()
                .account(accountQueryService.getReferenceById(accountId))
                .code(request.getCode())
                .department(departmentQueryService.getReferenceById(departmentInfo.id()))
                .hireDate(request.getHireDate())
                .build();

        EmployeeInfo saved = employeeInfoRepository.save(info);

        log.info("Employee {} created successfully", request.getUsername());
        return EmployeeResponse.from(saved, accountInfo, departmentInfo);
    }

    @Override
    protected void validate(CreateEmployeeRequest request) {
        super.validate(request);

        if (employeeInfoRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Employee code already assigned");
        }
    }

    @Override
    @Transactional
    public EmployeeResponse execute(CreateEmployeeRequest request) {
        return super.execute(request);
    }
}
