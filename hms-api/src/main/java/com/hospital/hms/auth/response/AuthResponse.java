package com.hospital.hms.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken,
                           Long expiresIn,
                           Long refreshExpiresIn) {
}
