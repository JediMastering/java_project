package com.example.first.config;

import com.example.first.entity.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || !(targetDomainObject instanceof String) || !(permission instanceof String)) {
            return false;
        }
        String targetType = ((String) targetDomainObject).toUpperCase();
        String requiredPermission = targetType + "_" + ((String) permission).toUpperCase();

        return hasPrivilege(authentication, requiredPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Não usaremos esta sobrecarga, mas é necessário implementá-la
        return false;
    }

    private boolean hasPrivilege(Authentication authentication, String requiredPermission) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(requiredPermission)) {
                return true;
            }
        }
        return false;
    }
}
