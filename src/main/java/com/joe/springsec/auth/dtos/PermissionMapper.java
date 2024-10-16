package com.joe.springsec.auth.dtos;

import com.joe.springsec.auth.models.Permission;

public class PermissionMapper {
    public static PermissionDTO toDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }

    public static Permission toEntity(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return permission;
    }
}
