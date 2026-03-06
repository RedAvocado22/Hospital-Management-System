package com.hospital.hms.department.repository;

import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.request.SearchDepartmentRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DepartmentSpecification {
    private DepartmentSpecification() {
    }

    public static Specification<Department> withFilters(SearchDepartmentRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null && !request.getName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + request.getName().toLowerCase() + "%"
                ));
            }

            if (request.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), request.getIsActive()));
            }

            if (request.getCreatedAt() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAt()));
            }

            if (request.getUpdatedAt() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), request.getUpdatedAt()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
