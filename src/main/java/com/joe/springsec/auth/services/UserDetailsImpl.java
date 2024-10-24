package com.joe.springsec.auth.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joe.springsec.auth.models.Permission;
import com.joe.springsec.auth.models.User;
import com.joe.springsec.company.model.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    // New field to store company IDs the user is associated with
    private List<Long> companyIds;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities, List<Long> companyIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.companyIds = companyIds; // Initialize companies
    }

    // Static method to build UserDetailsImpl from User entity
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role authorities
        authorities.addAll(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList()));

        // Add permission authorities
        authorities.addAll(user.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName())) // Assuming the Permission entity has a `getName()` method
                .collect(Collectors.toList()));

        // Assume the User entity has a method getCompanyIds() to retrieve associated companies
        List<Long> companyIds = user.getCompanies().stream()
                .map(Company::getId)
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities, // Pass combined authorities
                companyIds); // Add company IDs to user details
    }


    //Field to store user permissions
    //private List<String> permissions;

//    public UserDetailsImpl(Long id, String username, String email, String password,
//                           Collection<? extends GrantedAuthority> authorities, List<Long> companyIds, List<String> permissions) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.authorities = authorities;
//        this.companyIds = companyIds; // Initialize companies
//        //this.permissions = permissions;
//    }

//    // Static method to build UserDetailsImpl from User entity
//    public static UserDetailsImpl build(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());
//
//        // Assume the User entity has a method getCompanyIds() to retrieve associated companies
//        List<Long> companyIds = user.getCompanies().stream()
//                .map(Company::getId)
//                .collect(Collectors.toList());
//
//        List<String> permissions = user.getPermissions().stream()
//                .map(Permission::getName) // Assuming the Permission entity has a `getName()` method
//                .collect(Collectors.toList());
//
//        return new UserDetailsImpl(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getPassword(),
//                authorities,
//                companyIds,
//                permissions); // Add company IDs to user details
//    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public List<Long> getCompanyIds() {
        return companyIds;
    }

//    public List<String> getPermissions() {
//        return permissions; // Getter for permissions
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
