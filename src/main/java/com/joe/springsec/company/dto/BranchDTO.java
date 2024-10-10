package com.joe.springsec.company.dto;

import lombok.Data;

@Data
public class BranchDTO {

    private Long id;

    private String branchCode;

    private String branchName;

    //private Address address;

    //private AddressDTO address;

    private CompanyDTO company;

    private Long companyId;
}
