package com.example.first.dto;

import java.util.List;

public record UserDTO(Long id, String username, String profileImageUrl, List<Long> accessGroupIds) {
}
