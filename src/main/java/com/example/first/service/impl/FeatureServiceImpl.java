package com.example.first.service.impl;

import com.example.first.dto.FeatureDTO;
import com.example.first.entity.Feature;
import com.example.first.repository.FeatureRepository;
import com.example.first.service.FeatureService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public List<FeatureDTO> getFeatureTree() {
        List<Feature> rootFeatures = featureRepository.findByParentIsNull();
        return rootFeatures.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private FeatureDTO toDto(Feature feature) {
        List<String> permissions = new ArrayList<>();
        if (feature.isHasView()) permissions.add("view");
        if (feature.isHasCreate()) permissions.add("create");
        if (feature.isHasEdit()) permissions.add("edit");
        if (feature.isHasDelete()) permissions.add("delete");

        List<FeatureDTO> children = feature.getChildren().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new FeatureDTO(feature.getId(), feature.getName(), permissions, children);
    }
}
