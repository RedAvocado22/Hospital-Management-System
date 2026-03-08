package com.hospital.hms.auth.response;

import com.hospital.hms.auth.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account details returned after successful registration")
public record AccountResponse(
        @Schema(description = "Login username", example = "john.doe")
        String username,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Email address", example = "john.doe@email.com")
        String email,

        @Schema(description = "Full display name", example = "John Doe")
        String fullName,

        @Schema(description = "Assigned role", example = "PATIENT")
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
