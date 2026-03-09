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
                        request.getUpdatedAtRange().from().atStartOfDay(),
                        request.getUpdatedAtRange().to().atTime(23, 59, 59))
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
