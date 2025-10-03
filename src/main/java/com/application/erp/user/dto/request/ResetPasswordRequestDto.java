package com.application.erp.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(
        @NotBlank String token,
        @NotBlank String newPassword
) {
}
