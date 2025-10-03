package com.application.erp.accessgroup.dto;

import java.util.Map;

public record AccessGroupResponseDTO(
        Long id,
        String name,
        Map<Long, PermissionActionsDTO> permissions
) {}
