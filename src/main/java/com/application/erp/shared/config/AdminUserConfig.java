package com.application.erp.shared.config;

import com.application.erp.accessgroup.domain.entity.AccessGroup;
import com.application.erp.accessgroup.domain.entity.AccessGroupPermission;
import com.application.erp.feature.domain.entity.Feature;
import com.application.erp.user.domain.entity.User;
import com.application.erp.accessgroup.domain.repository.AccessGroupRepository;
import com.application.erp.feature.domain.repository.FeatureRepository;
import com.application.erp.user.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessGroupRepository accessGroupRepository;
    private final FeatureRepository featureRepository;

    public AdminUserConfig(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AccessGroupRepository accessGroupRepository,
                           FeatureRepository featureRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessGroupRepository = accessGroupRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Feature> featuresToCreate = Arrays.asList(
                new Feature("home", null, true, false, false, false),
                new Feature("users", null, true, true, true, true),
                new Feature("access-groups", null, true, true, true, true)
        );

        for (Feature feature : featuresToCreate) {
            featureRepository.findByName(feature.getName()).ifPresentOrElse(
                    existingFeature -> {
                        // Feature already exists, do nothing
                    },
                    () -> featureRepository.save(feature)
            );
        }

        AccessGroup superAdminGroup = accessGroupRepository.findByName("SUPER_ADMIN")
                .orElseGet(() -> {
                    AccessGroup newGroup = new AccessGroup();
                    newGroup.setName("SUPER_ADMIN");
                    return accessGroupRepository.save(newGroup);
                });

        List<Feature> allFeatures = featureRepository.findAll();

        Set<AccessGroupPermission> adminPermissions = new HashSet<>();
        for (Feature feature : allFeatures) {
            AccessGroupPermission permission = new AccessGroupPermission();
            permission.setAccessGroup(superAdminGroup);
            permission.setFeature(feature);
            permission.setCanView(true);
            permission.setCanCreate(true);
            permission.setCanEdit(true);
            permission.setCanDelete(true);
            adminPermissions.add(permission);
        }

        superAdminGroup.getPermissions().clear();
        superAdminGroup.getPermissions().addAll(adminPermissions);
        accessGroupRepository.save(superAdminGroup);

        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> {
                    if (!user.getAccessGroups().contains(superAdminGroup)) {
                        user.getAccessGroups().add(superAdminGroup);
                        userRepository.save(user);
                    }
                },
                () -> {
                    User adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setPassword(passwordEncoder.encode("password"));
                    adminUser.setEmail("admin@example.com");
                    adminUser.getAccessGroups().add(superAdminGroup);
                    userRepository.save(adminUser);
                }
        );
    }
}