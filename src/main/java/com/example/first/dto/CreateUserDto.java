package com.example.first.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateUserDto(
        @NotBlank @Size(min = 5, max = 255) String username,
        @NotBlank @Size(min = 8, max = 20) String password,
        @NotEmpty List<Long> accessGroupIds
) {
}
