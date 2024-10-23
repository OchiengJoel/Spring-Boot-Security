package com.joe.springsec.auth.web;

import com.joe.springsec.auth.dtos.UserDto;
import com.joe.springsec.auth.dtos.UserMapper;
import com.joe.springsec.auth.models.User;
import com.joe.springsec.auth.payload.response.MessageResponse;
import com.joe.springsec.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/company/{companyId}/list")
    public ResponseEntity<?> getUsersByCompanyId(@PathVariable Long companyId) {
        List<User> users = userService.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }

    // Endpoint to list all users
    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Endpoint to add a role to a user
    @PostMapping("/{userId}/roles/add")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long userId, @RequestParam String roleName) {
        User updatedUser = userService.addRoleToUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }
    // Endpoint to remove a role from a user
    @DeleteMapping("/{userId}/roles/remove")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long userId, @RequestParam String roleName) {
        User updatedUser = userService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'ADD_USER')")
    public ResponseEntity<MessageResponse> addUser(@RequestBody UserDto userDto) {
        // Implementation for adding a user
        return ResponseEntity.ok(new MessageResponse("User added successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        // Retrieve the user from the database
        User user = userService.getUserById(id); // Fetch User entity from the service

        // Use the mapper to convert User to UserDto
        UserDto userDto = UserMapper.toDto(user);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'DELETE_USER')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        // Implementation for deleting a user
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
}
