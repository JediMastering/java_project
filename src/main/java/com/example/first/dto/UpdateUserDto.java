package com.example.first.dto;

import java.util.List;

public record UpdateUserDto(
        String username,
        String password,
        List<Long> accessGroupIds
) {
}
