package com.hospital.hms.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Keycloak authentication tokens returned after successful login")
public record AuthResponse(
        @Schema(description = "JWT access token for API authorization", example = "eyJhbGciOiJSUzI1NiIs...")
        String accessToken,

        @Schema(description = "Refresh token to obtain a new access token", example = "eyJhbGciOiJIUzUxMiIs...")
        String refreshToken,

        @Schema(description = "Access token lifetime in seconds", example = "300")
        Long expiresIn,

        @Schema(description = "Refresh token lifetime in seconds", example = "1800")
        Long refreshExpiresIn) {
}
