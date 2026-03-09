package com.hospital.hms.auth.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Credentials for user authentication")
public class SignInRequest extends BaseRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    @Schema(description = "Account username", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 100)
    @Schema(description = "Account password", example = "Str0ng!Pass", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
