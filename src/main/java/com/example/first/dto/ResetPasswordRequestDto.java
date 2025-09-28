package com.example.first.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(
        @NotBlank String token,
        @NotBlank String newPassword
) {
}
