package com.hospital.hms.auth.response;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.entity.Role;

public record AccountResponse(
        String username,

        String firstName,

        String lastName,

        String email,

        String fullName,

        Role role
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getUsername(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getFullName(),
                account.getRole()
        );
    }
}
