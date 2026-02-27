package com.hospital.hms.pharmacy.repository;

import com.hospital.hms.pharmacy.dto.request.MedicineGetAllRequest;
import com.hospital.hms.pharmacy.entity.Medicine;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MedicineSpecification {

    private MedicineSpecification() {
    }

    public static Specification<Medicine> withFilters(MedicineGetAllRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getId() != null) {
            }

            if (request.getName() != null && !request.getName().isBlank()) {
                //add LIKE query
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
//                        -- LIKE operator (the keyword)
//                        WHERE title LIKE '...'
//
//                        -- % is the pattern/wildcard (the value)
//                        WHERE title LIKE '%java%'  -- matches anything containing "java"
//                        WHERE title LIKE 'java%'   -- matches anything starting with "java"
//                        WHERE title LIKE '%java'   -- matches anything ending with "java"
//                        WHERE title LIKE 'java'    -- exact match only
                        "%" + request.getName().toLowerCase() + "%"
                ));
            }
            //     this will add AND    this make List<Predicate> to Array
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            // -> after this will make a WHERE condition1 AND condition2 AND ...
        };
    }
}
