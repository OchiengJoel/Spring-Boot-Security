package com.joe.springsec.auth.payload.request;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AssignCompaniesRequest {

    private Long userId; // ID of the user to whom companies are assigned
    private List<Long> companyIds; // List of company IDs to assign

    public AssignCompaniesRequest() {
    }

    @Override
    public String toString() {
        return "AssignCompaniesRequest{" +
                "userId=" + userId +
                ", companyIds=" + companyIds +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<Long> companyIds) {
        this.companyIds = companyIds;
    }
}
