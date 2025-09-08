package com.example.first.web;

import com.example.first.dto.AccessGroupPayloadDTO;
import com.example.first.dto.AccessGroupResponseDTO;
import com.example.first.service.AccessGroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/access-groups")
public class AccessGroupController {

    private final AccessGroupService accessGroupService;

    public AccessGroupController(AccessGroupService accessGroupService) {
        this.accessGroupService = accessGroupService;
    }

    @PostMapping
    public ResponseEntity<AccessGroupResponseDTO> createAccessGroup(@RequestBody @Valid AccessGroupPayloadDTO payload) {
        AccessGroupResponseDTO createdGroup = accessGroupService.createAccessGroup(payload);
        return ResponseEntity.ok(createdGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessGroupResponseDTO> getAccessGroupById(@PathVariable Long id) {
        AccessGroupResponseDTO group = accessGroupService.findAccessGroupById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<Page<AccessGroupResponseDTO>> getAllAccessGroups(Pageable pageable) {
        Page<AccessGroupResponseDTO> groups = accessGroupService.findAllAccessGroups(pageable);
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessGroupResponseDTO> updateAccessGroup(@PathVariable Long id, @RequestBody @Valid AccessGroupPayloadDTO payload) {
        AccessGroupResponseDTO updatedGroup = accessGroupService.updateAccessGroup(id, payload);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessGroup(@PathVariable Long id) {
        accessGroupService.deleteAccessGroup(id);
        return ResponseEntity.noContent().build();
    }
}