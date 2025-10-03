package com.application.erp.accessgroup.dto;

import java.util.Map;

public record AccessGroupPayloadDTO(
        String name,
        Map<Long, PermissionActionsDTO> permissions
) {}
