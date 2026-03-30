package com.hospital.hms.patient.repository;

import com.hospital.hms.patient.entity.PatientInfo;
import com.hospital.hms.patient.request.SearchPatientRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PatientSpecification {

    private PatientSpecification() {
    }

    public static Specification<PatientInfo> withFilters(SearchPatientRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> accountJoin = null;

            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                accountJoin = root.join("account");
                String fullName = request.getFullName().toLowerCase();
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("fullName")),
                        "%" + fullName + "%"
                ));
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                if (accountJoin == null)
                    accountJoin = root.join("account");
                String email = request.getEmail().toLowerCase();
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(accountJoin.get("email")),
                        "%" + email + "%"
                ));
            }

            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                if (accountJoin == null)
                    accountJoin = root.join("account");
                String phone = request.getPhone();
                predicates.add(criteriaBuilder.like(
                        accountJoin.get("phone"),
                        "%" + phone + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

