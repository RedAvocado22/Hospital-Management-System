package com.hospital.hms.auth.entity;

import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "employee_info")
@AttributeOverride(name = "id", column = @Column(name = "employee_info_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeInfo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "employee_code", length = 20)
    private String employeeCode;
}
