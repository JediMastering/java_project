package com.example.first.service.impl;

import com.example.first.dto.AccessGroupPayloadDTO;
import com.example.first.dto.AccessGroupResponseDTO;
import com.example.first.dto.PermissionActionsDTO;
import com.example.first.entity.AccessGroup;
import com.example.first.entity.AccessGroupPermission;
import com.example.first.entity.Feature;
import com.example.first.exception.EntityNotFoundException;
import com.example.first.repository.AccessGroupPermissionRepository;
import com.example.first.repository.AccessGroupRepository;
import com.example.first.repository.FeatureRepository;
import com.example.first.service.AccessGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccessGroupServiceImpl implements AccessGroupService {

    private final AccessGroupRepository accessGroupRepository;
    private final FeatureRepository featureRepository;
    private final AccessGroupPermissionRepository accessGroupPermissionRepository;

    public AccessGroupServiceImpl(AccessGroupRepository accessGroupRepository, FeatureRepository featureRepository, AccessGroupPermissionRepository accessGroupPermissionRepository) {
        this.accessGroupRepository = accessGroupRepository;
        this.featureRepository = featureRepository;
        this.accessGroupPermissionRepository = accessGroupPermissionRepository;
    }

    @Override
    @Transactional
    public AccessGroupResponseDTO createAccessGroup(AccessGroupPayloadDTO payload) {
        AccessGroup accessGroup = new AccessGroup();
        accessGroup.setName(payload.name());

        Set<AccessGroupPermission> permissions = buildPermissions(accessGroup, payload.permissions());
        accessGroup.setPermissions(permissions);

        AccessGroup savedAccessGroup = accessGroupRepository.save(accessGroup);
        return toResponseDTO(savedAccessGroup);
    }

    @Override
    public AccessGroupResponseDTO findAccessGroupById(Long id) {
        AccessGroup accessGroup = accessGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, AccessGroup.class));
        return toResponseDTO(accessGroup);
    }

    @Override
    public Page<AccessGroupResponseDTO> findAllAccessGroups(Pageable pageable) {
        return accessGroupRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Override
    @Transactional
    public AccessGroupResponseDTO updateAccessGroup(Long id, AccessGroupPayloadDTO payload) {
        AccessGroup accessGroup = accessGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, AccessGroup.class));

        accessGroup.setName(payload.name());

        Map<Long, AccessGroupPermission> existingPermissionsMap = accessGroup.getPermissions().stream()
                .collect(Collectors.toMap(p -> p.getFeature().getId(), p -> p));

        Map<Long, PermissionActionsDTO> payloadPermissionsMap = payload.permissions();

        // Update existing permissions and add new ones
        for (Map.Entry<Long, PermissionActionsDTO> entry : payloadPermissionsMap.entrySet()) {
            Long featureId = entry.getKey();
            PermissionActionsDTO actions = entry.getValue();
            AccessGroupPermission existingPermission = existingPermissionsMap.get(featureId);

            if (existingPermission != null) {
                // Update existing permission
                existingPermission.setCanView(actions.view());
                existingPermission.setCanCreate(actions.create());
                existingPermission.setCanEdit(actions.edit());
                existingPermission.setCanDelete(actions.delete());
            } else {
                // Add new permission
                Feature feature = featureRepository.findById(featureId)
                        .orElseThrow(() -> new RuntimeException("Feature not found with id: " + featureId));
                AccessGroupPermission newPermission = new AccessGroupPermission();
                newPermission.setAccessGroup(accessGroup);
                newPermission.setFeature(feature);
                newPermission.setCanView(actions.view());
                newPermission.setCanCreate(actions.create());
                newPermission.setCanEdit(actions.edit());
                newPermission.setCanDelete(actions.delete());
                accessGroup.getPermissions().add(newPermission);
            }
        }

        // Remove permissions that are no longer in the payload
        Set<Long> payloadFeatureIds = payloadPermissionsMap.keySet();
        accessGroup.getPermissions().removeIf(permission -> !payloadFeatureIds.contains(permission.getFeature().getId()));

        AccessGroup updatedAccessGroup = accessGroupRepository.save(accessGroup);
        return toResponseDTO(updatedAccessGroup);
    }

    @Override
    @Transactional
    public void deleteAccessGroup(Long id) {
        if (!accessGroupRepository.existsById(id)) {
            throw new EntityNotFoundException(id, AccessGroup.class);
        }
        accessGroupRepository.deleteById(id);
    }

    private Set<AccessGroupPermission> buildPermissions(AccessGroup accessGroup, Map<Long, PermissionActionsDTO> permissionsPayload) {
        return permissionsPayload.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> {
                    Feature feature = featureRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Feature not found with id: " + entry.getKey()));

                    PermissionActionsDTO actions = entry.getValue();

                    AccessGroupPermission permission = new AccessGroupPermission();
                    permission.setAccessGroup(accessGroup);
                    permission.setFeature(feature);
                    permission.setCanView(actions.view());
                    permission.setCanCreate(actions.create());
                    permission.setCanEdit(actions.edit());
                    permission.setCanDelete(actions.delete());
                    return permission;
                })
                .collect(Collectors.toSet());
    }

    private AccessGroupResponseDTO toResponseDTO(AccessGroup accessGroup) {
        Map<Long, PermissionActionsDTO> permissionsMap = new HashMap<>();
        for (AccessGroupPermission agp : accessGroup.getPermissions()) {
            permissionsMap.put(agp.getFeature().getId(), new PermissionActionsDTO(
                    agp.isCanView(),
                    agp.isCanCreate(),
                    agp.isCanEdit(),
                    agp.isCanDelete()
            ));
        }
        return new AccessGroupResponseDTO(accessGroup.getAccessGroupId(), accessGroup.getName(), permissionsMap);
    }
}