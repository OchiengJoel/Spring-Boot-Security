package com.joe.springsec.auth.web;

import com.joe.springsec.auth.models.User;
import com.joe.springsec.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/company/{companyId}/users/list")
//    public ResponseEntity<List<User>> getUsersByCompanyId(@PathVariable Long companyId) {
//        List<User> users = userService.getUsersByCompanyId(companyId);
//        return ResponseEntity.ok(users);
//    }

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


}
