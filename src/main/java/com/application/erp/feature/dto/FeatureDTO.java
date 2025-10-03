package com.application.erp.feature.dto;

import java.util.List;

public record FeatureDTO(
        Long id,
        String name,
        List<String> permissions,
        List<FeatureDTO> children
) {}
