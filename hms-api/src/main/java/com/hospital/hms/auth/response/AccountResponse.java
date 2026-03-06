package com.hospital.hms.auth.response;

import com.hospital.hms.auth.entity.Account;

public record AccountResponse(
        String username,

        String firstName,

        String lastName,

        String email,

        String fullName,

        String role
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getUsername(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getFullName(),
                account.getRole().getName()
        );
    }
}
