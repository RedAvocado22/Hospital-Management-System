package com.hospital.hms.appointment.repository;

import com.hospital.hms.appointment.entity.Appointment;
import com.hospital.hms.appointment.request.SearchAppointmentRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentSpecification {
    private AppointmentSpecification() {
    }


    public static Specification<Appointment> withFilters(SearchAppointmentRequest request, UUID doctorId, UUID patientId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> patientJoin = null;
            Join<Object, Object> patientAccountJoin = null;
            Join<Object, Object> doctorAccountJoin;

            if (doctorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("doctor").get("id"), doctorId));
            }

            if (patientId != null) {
                predicates.add(criteriaBuilder.equal(root.get("patient").get("id"), patientId));
            }

            if (request.getDoctorName() != null && !request.getDoctorName().isBlank()) {
                doctorAccountJoin = root.join("doctor");
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(doctorAccountJoin.get("fullName")),
                        "%" + request.getDoctorName().toLowerCase() + "%"
                ));
            }

            if (request.getPatientName() != null && !request.getPatientName().isBlank()) {
                patientJoin = root.join("patient");
                patientAccountJoin = patientJoin.join("account");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(patientAccountJoin.get("fullName")),
                        "%" + request.getPatientName().toLowerCase() + "%"
                ));
            }

            if (request.getPatientEmail() != null && !request.getPatientEmail().isBlank()) {
                if (patientJoin == null) patientJoin = root.join("patient");
                if (patientAccountJoin == null) patientAccountJoin = patientJoin.join("account");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(patientAccountJoin.get("email")),
                        "%" + request.getPatientEmail().toLowerCase() + "%"
                ));
            }

            if (request.getPatientPhone() != null && !request.getPatientPhone().isBlank()) {
                if (patientJoin == null) patientJoin = root.join("patient");
                if (patientAccountJoin == null) patientAccountJoin = patientJoin.join("account");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(patientAccountJoin.get("phone")),
                        "%" + request.getPatientPhone().toLowerCase() + "%"
                ));
            }

            if (request.getFrom() != null && request.getTo() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("date"),
                        request.getFrom(),
                        request.getTo())
                );
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
