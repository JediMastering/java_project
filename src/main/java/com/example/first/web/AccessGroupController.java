package com.example.first.web;

import com.example.first.dto.AccessGroupPayloadDTO;
import com.example.first.dto.AccessGroupResponseDTO;
import com.example.first.service.AccessGroupService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access-groups")
public class AccessGroupController {

    private final AccessGroupService accessGroupService;

    public AccessGroupController(AccessGroupService accessGroupService) {
        this.accessGroupService = accessGroupService;
    }

    @PostMapping
    @PreAuthorize("hasPermission('access-groups', 'create')")
    public ResponseEntity<AccessGroupResponseDTO> createAccessGroup(@RequestBody @Valid AccessGroupPayloadDTO payload) {
        AccessGroupResponseDTO createdGroup = accessGroupService.createAccessGroup(payload);
        return ResponseEntity.ok(createdGroup);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('access-groups', 'view')")
    public ResponseEntity<AccessGroupResponseDTO> getAccessGroupById(@PathVariable Long id) {
        AccessGroupResponseDTO group = accessGroupService.findAccessGroupById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    @PreAuthorize("hasPermission('access-groups', 'view')")
    public ResponseEntity<Page<AccessGroupResponseDTO>> getAllAccessGroups(Pageable pageable) {
        Page<AccessGroupResponseDTO> groups = accessGroupService.findAllAccessGroups(pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/all")
    @PreAuthorize("hasPermission('access-groups', 'view')")
    public ResponseEntity<List<AccessGroupResponseDTO>> getAllAccessGroups() {
        List<AccessGroupResponseDTO> groups = accessGroupService.findAllAccessGroups();
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('access-groups', 'edit')")
    public ResponseEntity<AccessGroupResponseDTO> updateAccessGroup(@PathVariable Long id, @RequestBody @Valid AccessGroupPayloadDTO payload) {
        AccessGroupResponseDTO updatedGroup = accessGroupService.updateAccessGroup(id, payload);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('access-groups', 'delete')")
    public ResponseEntity<Void> deleteAccessGroup(@PathVariable Long id) {
        accessGroupService.deleteAccessGroup(id);
        return ResponseEntity.noContent().build();
    }
}
