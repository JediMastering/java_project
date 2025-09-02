package com.example.first.dto;

import java.util.Map;

public record PermissionActionsDTO(
        boolean view,
        boolean create,
        boolean edit,
        boolean delete
) {}
