package com.joe.springsec.auth.payload.response;

import java.util.List;

public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    //private List<String> permissions;
    private List<Long> companyIds;
    private String accessToken;

//    public UserInfoResponse(Long id, String username, String email, List<String> roles, List<String> permissions, List<Long> companyIds, String accessToken) {
      public UserInfoResponse(Long id, String username, String email, List<String> roles, List<Long> companyIds, String accessToken) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
       // this.permissions = permissions;
        this.companyIds = companyIds;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

//    public List<String> getPermissions() {
//        return permissions;
//    }
//
//    public void setPermissions(List<String> permissions) {
//        this.permissions = permissions;
//    }

    public List<Long> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<Long> companyIds) {
        this.companyIds = companyIds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
