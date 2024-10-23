package com.joe.springsec.mainconfig;

import com.joe.springsec.auth.enums.ERole;
import com.joe.springsec.auth.models.Permission;
import com.joe.springsec.auth.models.Role;
import com.joe.springsec.auth.repo.PermissionRepository;
import com.joe.springsec.auth.repo.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AppInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppInitializationConfig.class);

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        initializePermissions();
    }

    private void initializePermissions() {
        createPermission("ADD_USER", "Permission to add new users");
        createPermission("DELETE_USER", "Permission to delete users");
        createPermission("EDIT_USER", "Permission to edit users");
        createPermission("LIST_USERS", "Permission to list users");

        //assignPermissionsToAdminRole();
    }

    private void createPermission(String name, String description) {
        try {
            Optional<Permission> existingPermission = permissionRepository.findByName(name);
            if (!existingPermission.isPresent()) {
                Permission permission = new Permission();
                permission.setName(name);
                permission.setDescription(description);
                permissionRepository.save(permission);
                logger.info("Created permission: {}", name);
            } else {
                logger.warn("Permission {} already exists, skipping creation.", name);
            }
        } catch (Exception e) {
            logger.error("Error creating permission {}: {}", name, e.getMessage());
        }
    }
}

//    private void createPermission(String name, String description) {
//        try {
//            if (permissionRepository.findByName(name) == null) {
//                Permission permission = new Permission();
//                permission.setName(name);
//                permission.setDescription(description);
//                permissionRepository.save(permission);
//                logger.info("Created permission: {}", name);
//            } else {
//                logger.warn("Permission {} already exists, skipping creation.", name);
//            }
//        } catch (Exception e) {
//            logger.error("Error creating permission {}: {}", name, e.getMessage());
//        }
//    }

//    private void assignPermissionsToAdminRole() {
//        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//        Set<Permission> permissions = new HashSet<>();
//
//        for (String permissionName : new String[]{"ADD_USER", "DELETE_USER", "EDIT_USER"}) {
//            Optional<Permission> permission = permissionRepository.findByName(permissionName);
//            permission.ifPresent(permissions::add);
//        }
//
//        adminRole.setPermissions(permissions);
//        roleRepository.save(adminRole);
//    }


