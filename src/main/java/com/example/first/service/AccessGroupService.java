package com.example.first.service;

import com.example.first.dto.AccessGroupPayloadDTO;
import com.example.first.dto.AccessGroupResponseDTO;

import java.util.List;

public interface AccessGroupService {
    AccessGroupResponseDTO createAccessGroup(AccessGroupPayloadDTO payload);
    AccessGroupResponseDTO findAccessGroupById(Long id);
    List<AccessGroupResponseDTO> findAllAccessGroups();
    AccessGroupResponseDTO updateAccessGroup(Long id, AccessGroupPayloadDTO payload);
    void deleteAccessGroup(Long id);
}