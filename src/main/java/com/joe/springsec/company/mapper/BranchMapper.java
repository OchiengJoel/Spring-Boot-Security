package com.joe.springsec.company.mapper;

import com.joe.springsec.company.dto.BranchDTO;
import com.joe.springsec.company.model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    private final CompanyMapper companyMapper;
    //private final AddressMapper addressMapper;

    @Autowired
    public BranchMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
      //  this.addressMapper = addressMapper;
    }

    // Convert Branch entity to BranchDTO
    public BranchDTO toDTO(Branch branch) {
        if (branch == null) {
            return null;
        }
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setBranchCode(branch.getBranchCode());
        dto.setBranchName(branch.getBranchName());
       // dto.setAddress(addressMapper.toDTO(branch.getAddress())); // Convert Address to AddressDTO
        dto.setCompany(companyMapper.toDTO(branch.getCompany())); // Convert Company to CompanyDTO
        dto.setCompanyId(branch.getCompany().getId()); // If companyId is needed separately
        return dto;
    }

    // Convert BranchDTO to Branch entity
    public Branch toEntity(BranchDTO dto) {
        if (dto == null) {
            return null;
        }
        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setBranchCode(dto.getBranchCode());
        branch.setBranchName(dto.getBranchName());
        //branch.setAddress(addressMapper.toEntity(dto.getAddress())); // Convert AddressDTO to Address
        branch.setCompany(companyMapper.toEntity(dto.getCompany())); // Convert CompanyDTO to Company
        return branch;
    }

//    private AddressDTO toAddressDTO(Address address) {
//        if (address == null) {
//            return null;
//        }
//        AddressDTO dto = new AddressDTO();
//        dto.setEmail(address.getEmail());
//        dto.setContact(address.getContact());
//        dto.setCountry(address.getCountry());
//        dto.setCity(address.getCity());
//        dto.setLocation(address.getLocation());
//        dto.setRegNo(address.getRegNo());
//        dto.setPostalAddress(address.getPostalAddress());
//        return dto;
//    }
//
//    private Address toAddressEntity(AddressDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//        Address address = new Address();
//        address.setEmail(dto.getEmail());
//        address.setContact(dto.getContact());
//        address.setCountry(dto.getCountry());
//        address.setCity(dto.getCity());
//        address.setLocation(dto.getLocation());
//        address.setRegNo(dto.getRegNo());
//        address.setPostalAddress(dto.getPostalAddress());
//        return address;
//    }
}
