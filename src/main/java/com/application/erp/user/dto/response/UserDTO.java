package com.application.erp.user.dto.response;

import java.util.List;

public record UserDTO(Long userId, String username, String email, String profileImageUrl, List<Long> accessGroupIds) {
}
