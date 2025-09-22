package com.example.first.config;

import com.example.first.entity.AccessGroupPermission;
import com.example.first.entity.Feature;
import com.example.first.entity.User;
import com.example.first.repository.FeatureRepository;
import com.example.first.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    private final UserRepository userRepository;
    private final FeatureRepository featureRepository;

    public CustomPermissionEvaluator(UserRepository userRepository, FeatureRepository featureRepository) {
        this.userRepository = userRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.info("Checking permission for user: {}, target: {}, permission: {}", authentication.getName(), targetDomainObject, permission);

        if ((authentication == null) || !(targetDomainObject instanceof String) || !(permission instanceof String)) {
            logger.warn("Invalid permission check request. Authentication, target, or permission is not a String.");
            return false;
        }

        String featureName = (String) targetDomainObject;
        String action = (String) permission;

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (user == null) {
            logger.warn("User not found: {}", userId);
            return false;
        }

        Feature feature = featureRepository.findByName(featureName).orElse(null);
        if (feature == null) {
            logger.warn("Feature not found: {}", featureName);
            return false;
        }

        boolean hasPermission = user.getAccessGroups().stream()
                .flatMap(ag -> ag.getPermissions().stream())
                .anyMatch(p -> p.getFeature().getId().equals(feature.getId()) && hasActionPermission(p, action));

        logger.info("User {} has permission for action {} on feature {}: {}", userId, action, featureName, hasPermission);
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false; // Not used
    }

    private boolean hasActionPermission(AccessGroupPermission permission, String action) {
        return switch (action) {
            case "view" -> permission.isCanView();
            case "create" -> permission.isCanCreate();
            case "edit" -> permission.isCanEdit();
            case "delete" -> permission.isCanDelete();
            default -> false;
        };
    }
}
