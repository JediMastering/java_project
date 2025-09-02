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
import java.util.List;
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
    public List<AccessGroupResponseDTO> findAllAccessGroups() {
        return accessGroupRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccessGroupResponseDTO updateAccessGroup(Long id, AccessGroupPayloadDTO payload) {
        AccessGroup accessGroup = accessGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, AccessGroup.class));

        accessGroup.setName(payload.name());

        // Limpar permissões antigas
        accessGroup.getPermissions().clear();
        accessGroupPermissionRepository.deleteAll(accessGroup.getPermissions()); // Garantir que são deletadas do banco

        // Adicionar novas permissões
        Set<AccessGroupPermission> newPermissions = buildPermissions(accessGroup, payload.permissions());
        accessGroup.setPermissions(newPermissions);

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