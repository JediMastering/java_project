package com.application.erp.accessgroup.domain.service;

import com.application.erp.accessgroup.dto.AccessGroupPayloadDTO;
import com.application.erp.accessgroup.dto.AccessGroupResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccessGroupService {
    AccessGroupResponseDTO createAccessGroup(AccessGroupPayloadDTO payload);
    AccessGroupResponseDTO findAccessGroupById(Long id);
    Page<AccessGroupResponseDTO> findAllAccessGroups(Pageable pageable);
    List<AccessGroupResponseDTO> findAllAccessGroups();
    AccessGroupResponseDTO updateAccessGroup(Long id, AccessGroupPayloadDTO payload);
    void deleteAccessGroup(Long id);
}
