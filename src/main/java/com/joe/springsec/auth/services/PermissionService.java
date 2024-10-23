package com.joe.springsec.auth.services;

import com.joe.springsec.auth.models.Permission;
import com.joe.springsec.auth.repo.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long permissionId, Permission permission) {
        Permission existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found")); // Handle with custom exception
        existingPermission.setName(permission.getName());
        existingPermission.setDescription(permission.getDescription());
        return permissionRepository.save(existingPermission);
    }

    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionRepository.delete(permission);
    }

    public Page<Permission> getAllPermissions(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }
}
