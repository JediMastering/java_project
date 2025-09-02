package com.example.first.dto;

import java.util.List;

public record FeatureDTO(
        Long id,
        String name,
        List<String> permissions,
        List<FeatureDTO> children
) {}
