package com.hospital.hms.auth.dto;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.common.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record AccountInfo(
        UUID id,
        String username,
        String email,
        String fullName,
        LocalDate dob,
        Gender gender,
        String address,
        String phone,
        boolean active,
        String role
) {
    public static AccountInfo from(Account account) {
        return new AccountInfo(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getFullName(),
                account.getDob(),
                account.getGender(),
                account.getAddress(),
                account.getPhone(),
                account.getIsActive(),
                account.getRole().getName()
        );
    }
}
