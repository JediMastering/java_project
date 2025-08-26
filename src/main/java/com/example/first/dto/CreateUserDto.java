package com.example.first.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank @Size(min = 5, max = 255) String username,
        @NotBlank @Size(min = 8, max = 20) String password
) {
}
