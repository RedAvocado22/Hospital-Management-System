package com.hospital.hms.employee.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.request.EmployeeIdRequest;
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
public class DeActiveEmployeeService extends BaseService<EmployeeIdRequest, Void> {
    private final EmployeeInfoRepository employeeInfoRepository;
    private final KeycloakService keycloakService;

    @Override
    protected Void doProcess(EmployeeIdRequest request) {
        EmployeeInfo employee = employeeInfoRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Employee with id: " + request.getId() + " not found")
        );

        employee.getAccount().setIsActive(false);
        employeeInfoRepository.save(employee);

        try {
            keycloakService.setUserEnabled(employee.getAccount().getId().toString(), false);
        } catch (Exception e) {
            log.error("Keycloak error during update status - rolling back", e);
            throw new IdentityProviderException("Failed to update account in Keycloak — rolled back");
        }
        return null;
    }

    @Override
    @Transactional
    public Void execute(EmployeeIdRequest request) {
        return super.execute(request);
    }
}
