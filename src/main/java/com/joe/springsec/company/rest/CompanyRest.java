package com.joe.springsec.company.rest;

import com.joe.springsec.company.dto.CompanyDTO;
import com.joe.springsec.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/company")
@CrossOrigin("http://localhost:4200")
public class CompanyRest {

    private final CompanyService companyService;

    @Autowired
    public CompanyRest(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CompanyDTO>> findAll() {
        List<CompanyDTO> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            CompanyDTO company = companyService.getById(id);
            return ResponseEntity.ok(company);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company With Id Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> createCompany(@RequestBody CompanyDTO companyDTO) {
        try {
            companyService.createCompany(companyDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company created successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Saving Company: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Saving Company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        if (!id.equals(companyDTO.getId())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company Id mismatch");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            companyService.updateCompany(companyDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Updated Successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Updating Company: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Updating Company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company Deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Deleting Company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}


 /*private final CompanyService companyService;

    @Autowired
    public CompanyRest(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Company>> findAll(){
        List<Company> companies = companyService.findAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
        //return companyService.findAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){

        try {
            Company company = companyService.getById(id);
            return new ResponseEntity<>(company, HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException("Company With Id Not Found");
        }

    }

    @PostMapping("/save")
    public ResponseEntity<?> createCompany(@RequestBody Company company){
        try {
            companyService.createCompany(company);
            return new ResponseEntity<>("Company created successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error Saving Company" + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Company company ){
        if (!id.equals(company.getId())){
            return new ResponseEntity<>("Company Not Found", HttpStatus.BAD_REQUEST);
        }

        try {
            companyService.udpdateCompany(company);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error Updating Company" + e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id){
        companyService.deleteCompany(id);
        return new ResponseEntity<>("Company Deleted", HttpStatus.OK);
    }*/
