package com.joe.springsec.company.dto;


import lombok.Data;

import java.util.List;

@Data
public class CompanyDTO {

    private Long id;
    private String companyName;
    //private AddressDTO address;
   // private List<ProjectDTO> projects; // Add this field
}
