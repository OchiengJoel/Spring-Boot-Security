package com.joe.springsec.company.mapper;


import com.joe.springsec.company.dto.CompanyDTO;
import com.joe.springsec.company.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    private CompanyMapper() {
        // private constructor to prevent instantiation
    }
    public static CompanyDTO toDTO(Company company) {
        if (company == null) {
            return null;
        }
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        return dto;
    }


    public static Company toEntity(CompanyDTO dto) {
        if (dto == null) {
            return null;
        }
        Company company = new Company();
        company.setId(dto.getId());
        company.setCompanyName(dto.getCompanyName());
        return company;
    }

}
