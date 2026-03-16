package com.hospital.hms.employee.repository;

import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.request.SearchEmployeeRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {
    private EmployeeSpecification() {
    }

    public static Specification<EmployeeInfo> withFilters(SearchEmployeeRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> accountJoin = root.join("account");

            if (request.getName() != null && !request.getName().isBlank()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(accountJoin.get("username")),
                                "%" + request.getName().toLowerCase() + "%"
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(accountJoin.get("fullName")),
                                "%" + request.getName().toLowerCase() + "%"
                        )
                ));
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("email")),
                        "%" + request.getEmail().toLowerCase() + "%"
                ));
            }

            if (request.getDob() != null) {
                predicates.add(criteriaBuilder.between(
                        accountJoin.get("dob"),
                        request.getDob().from(),
                        request.getDob().to())
                );
            }

            if (request.getGender() != null) {
                predicates.add(criteriaBuilder.equal(accountJoin.get("gender"), request.getGender()));
            }

            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        accountJoin.get("phone"),
                        "%" + request.getPhone() + "%"
                ));
            }

            if (request.getAddress() != null && !request.getAddress().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("address")),
                        "%" + request.getAddress().toLowerCase() + "%"
                ));
            }

            if (request.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(accountJoin.get("isActive"), request.getIsActive()));
            }

            if (request.getCode() != null && !request.getCode().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("code")),
                        "%" + request.getCode().toLowerCase() + "%"
                ));
            }

            if (request.getDepartment() != null) {
                Join<Object, Object> departmentJoin = root.join("department");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(departmentJoin.get("name")),
                        "%" + request.getDepartment().toLowerCase() + "%"
                ));
            }

            if (request.getHireDate() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("hireDate"),
                        request.getHireDate().from().atStartOfDay(),
                        request.getHireDate().to().atTime(23, 59, 59))
                );
            }

            if (request.getCreatedAtRange() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("createdAt"),
                        request.getCreatedAtRange().from().atStartOfDay(),
                        request.getCreatedAtRange().to().atTime(23, 59, 59))
                );
            }

            if (request.getUpdatedAtRange() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("updatedAt"),
                        request.getUpdatedAtRange().from(),
                        request.getUpdatedAtRange().to())
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
