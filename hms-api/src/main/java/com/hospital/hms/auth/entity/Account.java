package com.hospital.hms.auth.entity;

import com.hospital.hms.base.BaseEntity;
import com.hospital.hms.common.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Account extends BaseEntity {

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @PrePersist
    @PreUpdate
    protected void prepareFullName() {
        String first = this.firstName != null ? this.firstName : "";
        String last = this.lastName != null ? this.lastName : "";
        this.fullName = (first + " " + last).trim();
    }
}
