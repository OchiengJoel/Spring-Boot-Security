package com.joe.springsec.auth.web;

import com.joe.springsec.auth.dtos.PermissionDTO;
import com.joe.springsec.auth.dtos.PermissionMapper;
import com.joe.springsec.auth.models.Permission;
import com.joe.springsec.auth.payload.response.ErrorResponse;
import com.joe.springsec.auth.payload.response.PermissionNotFoundException;
import com.joe.springsec.auth.payload.response.UserNotFoundException;
import com.joe.springsec.auth.services.PermissionService;
import com.joe.springsec.auth.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final UserService userService;

    public PermissionController(PermissionService permissionService, UserService userService) {
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        Permission createdPermission = permissionService.createPermission(PermissionMapper.toEntity(permissionDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionMapper.toDTO(createdPermission));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long permissionId, @Valid @RequestBody PermissionDTO permissionDTO) {
        Permission updatedPermission = permissionService.updatePermission(permissionId, PermissionMapper.toEntity(permissionDTO));
        return ResponseEntity.ok(PermissionMapper.toDTO(updatedPermission));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(Pageable pageable) {
        Page<Permission> permissions = permissionService.getAllPermissions(pageable);
        return ResponseEntity.ok(permissions.map(PermissionMapper::toDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/permissions/{permissionName}")
    public ResponseEntity<?> assignPermissionToUser(@PathVariable Long userId, @PathVariable String permissionName) {
        try {
            userService.addPermissionToUser(userId, permissionName);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found", HttpStatus.NOT_FOUND.value()));
        } catch (PermissionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Permission not found", HttpStatus.NOT_FOUND.value()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/permissions/{permissionName}")
    public ResponseEntity<?> removePermissionFromUser(@PathVariable Long userId, @PathVariable String permissionName) {
        try {
            userService.removePermissionFromUser(userId, permissionName);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found", HttpStatus.NOT_FOUND.value()));
        } catch (PermissionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Permission not found", HttpStatus.NOT_FOUND.value()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsForUser(@PathVariable Long userId) {
        List<Permission> userPermissions = userService.getUserPermissions(userId);
        return ResponseEntity.ok(userPermissions.stream().map(PermissionMapper::toDTO).collect(Collectors.toList()));
    }
}
