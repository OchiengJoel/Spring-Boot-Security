package com.joe.springsec.auth.dtos;

import com.joe.springsec.auth.models.Permission;
import lombok.Data;

@Data
public class PermissionDTO {

    private Long id;
    private String name;
    private String description;
    // Getters and setters
}

