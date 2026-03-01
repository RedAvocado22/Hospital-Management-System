package com.hospital.hms.auth.response;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken,
                           Long expiresIn,
                           Long refreshExpiresIn) {
}
