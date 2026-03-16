package com.hospital.hms.employee.service;

import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.RoleRepository;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.repository.DepartmentRepository;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.UpdateEmployeeRequest;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.IdentityProviderException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateEmployeeService extends BaseService<UpdateEmployeeRequest, EmployeeResponse> {
    private final EmployeeInfoRepository employeeInfoRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final KeycloakService keycloakService;

    @Override
    protected EmployeeResponse doProcess(UpdateEmployeeRequest request) {
        EmployeeInfo employee = employeeInfoRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Employee not found: " + request.getId())
        );

        if (request.getCode() != null
                && !request.getCode().isEmpty()
                && !employee.getCode().equals(request.getCode())
                && employeeInfoRepository.existsByCode(request.getCode())
        ) {
            throw new DuplicateResourceException("Code already exists: " + request.getCode());
        }


        if (request.getFirstName() != null) employee.getAccount().setFirstName(request.getFirstName());
        if (request.getLastName() != null) employee.getAccount().setLastName(request.getLastName());
        if (request.getDob() != null) employee.getAccount().setDob(request.getDob());
        if (request.getGender() != null) employee.getAccount().setGender(request.getGender());
        if (request.getAddress() != null) employee.getAccount().setAddress(request.getAddress());
        if (request.getPhone() != null) employee.getAccount().setPhone(request.getPhone());
        if (request.getCode() != null) employee.setCode(request.getCode());
        if (request.getHireDate() != null) employee.setHireDate(request.getHireDate());

        String oldRoleName = employee.getAccount().getRole().getName();

        if (request.getRole() != null) {
            Role role = roleRepository.findByNameIgnoreCase(request.getRole())
                    .orElseThrow(() -> new NotFoundException("Role not found: " + request.getRole()));
            employee.getAccount().setRole(role);
        }
        if (request.getDepartment() != null) {
            Department department = departmentRepository.findByNameIgnoreCase(request.getDepartment())
                    .orElseThrow(() -> new NotFoundException("Department not found: " + request.getDepartment()));
            employee.setDepartment(department);
        }

        EmployeeInfo updated = employeeInfoRepository.save(employee);

        if (request.getRole() != null) {
            try {
                keycloakService.updateRole(employee.getAccount().getId().toString(), request.getRole(), oldRoleName);
            } catch (Exception e) {
                log.error("Keycloak error during update role - rolling back", e);
                throw new IdentityProviderException("Failed to update account in Keycloak — rolled back");
            }
        }

        return EmployeeResponse.from(updated);
    }

    @Override
    protected void validate(UpdateEmployeeRequest request) {
        super.validate(request);
    }

    @Override
    @Transactional
    public EmployeeResponse execute(UpdateEmployeeRequest request) {
        return super.execute(request);
    }
}
