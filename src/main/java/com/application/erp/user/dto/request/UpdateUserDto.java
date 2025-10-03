package com.application.erp.user.dto.request;

import java.util.List;

public record UpdateUserDto(
        String username,
        String email,
        String password,
        List<Long> accessGroupIds
) {
}
