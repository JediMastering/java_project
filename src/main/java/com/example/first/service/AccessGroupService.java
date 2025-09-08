package com.example.first.service;

import com.example.first.dto.AccessGroupPayloadDTO;
import com.example.first.dto.AccessGroupResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessGroupService {
    AccessGroupResponseDTO createAccessGroup(AccessGroupPayloadDTO payload);
    AccessGroupResponseDTO findAccessGroupById(Long id);
    Page<AccessGroupResponseDTO> findAllAccessGroups(Pageable pageable);
    AccessGroupResponseDTO updateAccessGroup(Long id, AccessGroupPayloadDTO payload);
    void deleteAccessGroup(Long id);
}