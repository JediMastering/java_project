package com.example.first.dto;

import java.util.Map;

public record AccessGroupPayloadDTO(
        String name,
        Map<Long, PermissionActionsDTO> permissions
) {}
