package com.hospital.hms.employee.service;

import com.hospital.hms.auth.service.RoleQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.department.dto.DepartmentInfo;
import com.hospital.hms.department.service.DepartmentQueryService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.UpdateEmployeeRequest;
import com.hospital.hms.employee.response.EmployeeDetailResponse;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.IdentityProviderException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateEmployeeService extends BaseService<UpdateEmployeeRequest, EmployeeDetailResponse> {
    private final EmployeeInfoRepository employeeInfoRepository;
    private final KeycloakService keycloakService;
    private final DepartmentQueryService departmentQueryService;
    private final RoleQueryService roleQueryService;

    @Override
    protected EmployeeDetailResponse doProcess(UpdateEmployeeRequest request) {
        EmployeeInfo employee = employeeInfoRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Employee not found: " + request.getId())
        );

        String oldRoleName = null;

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

        if (request.getRole() != null) {
            oldRoleName = employee.getAccount().getRole().getName();
            UUID roleId = roleQueryService.getRoleIdByName(request.getRole());
            employee.getAccount().setRole(roleQueryService.getReferenceById(roleId));
        }
        if (request.getDepartment() != null) {
            DepartmentInfo departmentInfo = departmentQueryService.getDepartmentIdByName(request.getDepartment());
            employee.setDepartment(departmentQueryService.getReferenceById(departmentInfo.id()));
        }

        employeeInfoRepository.save(employee);

        if (request.getRole() != null) {
            try {
                keycloakService.updateRole(employee.getAccount().getId().toString(), request.getRole(), oldRoleName);
            } catch (Exception e) {
                log.error("Keycloak error during update role - rolling back", e);
                throw new IdentityProviderException("Failed to update account in Keycloak — rolled back");
            }
        }

        return EmployeeDetailResponse.from(employee);
    }

    @Override
    @Transactional
    public EmployeeDetailResponse execute(UpdateEmployeeRequest request) {
        return super.execute(request);
    }
}
