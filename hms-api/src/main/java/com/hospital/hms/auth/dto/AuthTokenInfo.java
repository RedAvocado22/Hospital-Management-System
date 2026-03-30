package com.hospital.hms.auth.dto;

public record AuthTokenInfo(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        Long refreshExpiresIn
) {
}
