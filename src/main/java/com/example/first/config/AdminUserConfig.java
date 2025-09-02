package com.example.first.config;

import com.example.first.entity.AccessGroup;
import com.example.first.entity.AccessGroupPermission;
import com.example.first.entity.Feature;
import com.example.first.entity.User;
import com.example.first.repository.AccessGroupPermissionRepository;
import com.example.first.repository.AccessGroupRepository;
import com.example.first.repository.FeatureRepository;
import com.example.first.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccessGroupRepository accessGroupRepository;
    private final FeatureRepository featureRepository;
    private final AccessGroupPermissionRepository accessGroupPermissionRepository;

    public AdminUserConfig(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           AccessGroupRepository accessGroupRepository,
                           FeatureRepository featureRepository,
                           AccessGroupPermissionRepository accessGroupPermissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessGroupRepository = accessGroupRepository;
        this.featureRepository = featureRepository;
        this.accessGroupPermissionRepository = accessGroupPermissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. Criar ou encontrar o AccessGroup 'SUPER_ADMIN'
        AccessGroup superAdminGroup = accessGroupRepository.findByName("SUPER_ADMIN")
                .orElseGet(() -> {
                    AccessGroup newGroup = new AccessGroup();
                    newGroup.setName("SUPER_ADMIN");
                    return accessGroupRepository.save(newGroup);
                });

        // 2. Obter todas as Features
        List<Feature> allFeatures = featureRepository.findAll();

        // 3. Criar AccessGroupPermissions para todas as Features com todas as permissões
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

        // Limpar permissões antigas e adicionar as novas
        superAdminGroup.getPermissions().clear();
        superAdminGroup.getPermissions().addAll(adminPermissions);
        accessGroupRepository.save(superAdminGroup);

        // 4. Criar ou encontrar o usuário 'admin'
        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> {
                    // Se o usuário admin já existe, garantir que ele está no grupo SUPER_ADMIN
                    if (!user.getAccessGroups().contains(superAdminGroup)) {
                        user.getAccessGroups().add(superAdminGroup);
                        userRepository.save(user);
                    }
                    System.out.println("Usuário admin já existe e está no grupo SUPER_ADMIN.");
                },
                () -> {
                    // Se o usuário admin não existe, criar e associar ao grupo SUPER_ADMIN
                    User adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setPassword(passwordEncoder.encode("123")); // Senha padrão
                    adminUser.setEmail("admin@example.com"); // Email padrão
                    adminUser.getAccessGroups().add(superAdminGroup);
                    userRepository.save(adminUser);
                    System.out.println("Usuário admin criado e associado ao grupo SUPER_ADMIN.");
                }
        );
    }
}