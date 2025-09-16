package com.example.first.dto;

import java.util.List;
import java.util.UUID;

public record UserDTO(UUID id, String username, String profileImageUrl, List<Long> accessGroupIds) {
}
