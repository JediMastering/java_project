package com.application.erp.feature.controller;

import com.application.erp.feature.dto.FeatureDTO;
import com.application.erp.feature.domain.service.FeatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    public ResponseEntity<List<FeatureDTO>> getPermissions() {
        List<FeatureDTO> permissions = featureService.getFeatureTree();
        return ResponseEntity.ok(permissions);
    }
}