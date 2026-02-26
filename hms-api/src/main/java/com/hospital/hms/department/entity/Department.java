package com.hospital.hms.department.entity;

import com.hospital.hms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "department")
@AttributeOverride(name = "id", column = @Column(name = "department_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Department extends BaseEntity {

    @Column(name = "department_name", length = 100, nullable = false)
    private String departmentName;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isActive;
}
