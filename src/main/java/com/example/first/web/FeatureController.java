package com.example.first.web;

import com.example.first.dto.FeatureDTO;
import com.example.first.service.FeatureService;
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