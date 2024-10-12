package com.joe.springsec.auth.services;

import com.joe.springsec.auth.enums.ERole;
import com.joe.springsec.auth.models.Role;
import com.joe.springsec.auth.models.User;
import com.joe.springsec.auth.repo.RoleRepository;
import com.joe.springsec.auth.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    public List<User> getUsersByCompanyId(Long companyId){
        return userRepository.findUsersByCompanyId(companyId);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // Method to add a role to a user
    @Transactional
    public User addRoleToUser(Long userId, String roleName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Convert String roleName to ERole enum
            ERole roleEnum;
            try {
                roleEnum = ERole.valueOf(roleName);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role name: " + roleName);
            }

            Optional<Role> roleOptional = roleRepository.findByName(roleEnum);
            if (roleOptional.isPresent()) {
                Role role = roleOptional.get();
                user.getRoles().add(role);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Role not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Method to remove a role from a user
    @Transactional
    public User removeRoleFromUser(Long userId, String roleName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Convert String roleName to ERole enum
            ERole roleEnum;
            try {
                roleEnum = ERole.valueOf(roleName);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role name: " + roleName);
            }

            Optional<Role> roleOptional = roleRepository.findByName(roleEnum);
            if (roleOptional.isPresent()) {
                Role role = roleOptional.get();
                user.getRoles().remove(role);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Role not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
