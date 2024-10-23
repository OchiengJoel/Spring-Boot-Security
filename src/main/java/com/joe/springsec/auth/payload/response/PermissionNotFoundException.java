package com.joe.springsec.auth.payload.response;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(String permissionName) {
        super("Permission not found: " + permissionName);
    }
}
