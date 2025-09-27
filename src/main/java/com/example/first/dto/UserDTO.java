package com.example.first.dto;

import java.util.List;

public record UserDTO(Long userId, String username, String email, String profileImageUrl, List<Long> accessGroupIds) {
}
