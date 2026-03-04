package com.hospital.hms.employee.service;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.auth.request.AccountRegistrationRequest;
import com.hospital.hms.auth.service.AccountRegistrationService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.repository.DepartmentRepository;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.CreateEmployeeRequest;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateEmployeeService extends BaseService<CreateEmployeeRequest, Void> {

    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeInfoRepository employeeInfoRepository;
    private final AccountRegistrationService accountRegistrationService;

    @Override
    @Transactional
    protected Void doProcess(CreateEmployeeRequest request) {
        log.info("Starting employee creation for username: {}", request.getUsername());

        Role role = roleRepository.findByNameIgnoreCase(request.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found: " + request.getRole()));

        Department department = departmentRepository.findByNameIgnoreCase(request.getDepartment())
                .orElseThrow(() -> new NotFoundException("Department not found: " + request.getDepartment()));

        AccountRegistrationRequest accountRegistrationRequest = AccountRegistrationRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .role(role)
                .build();

        Account account = accountRegistrationService.execute(accountRegistrationRequest);

        EmployeeInfo info = EmployeeInfo.builder()
                .account(account)
                .code(request.getCode())
                .department(department)
                .hireDate(request.getHireDate())
                .build();

        employeeInfoRepository.save(info);

        log.info("Employee {} created successfully", request.getUsername());
        return null;
    }

    @Override
    protected void validate(CreateEmployeeRequest request) {
        super.validate(request);

        if (employeeInfoRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Employee code already assigned");
        }
    }
}
