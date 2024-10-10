package com.joe.springsec.company.rest;


import com.joe.springsec.company.dto.BranchDTO;
import com.joe.springsec.company.model.Branch;
import com.joe.springsec.company.service.BranchService;
import com.joe.springsec.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/company/{companyId}/branches")
public class BranchRest {

    private final BranchService branchService;
    private final CompanyService companyService;

    public BranchRest(BranchService branchService, CompanyService companyService) {
        this.branchService = branchService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<BranchDTO>> listBranchesByCompany(@PathVariable Long companyId) {
        List<BranchDTO> branches = branchService.listAllByCompanyId(companyId);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchById(@PathVariable Long companyId, @PathVariable Long id) {
        try {
            Branch branch = branchService.findByIdAndCompanyId(id, companyId);
            BranchDTO branchDTO = branchService.toDTO(branch); // Convert Branch to BranchDTO
            return ResponseEntity.ok(branchDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Branch with Id Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> createBranch(@PathVariable Long companyId, @RequestBody BranchDTO branchDTO) {
        try {
            companyService.getById(companyId); // Ensure company exists
            branchDTO.setCompany(companyService.getById(companyId)); // Set company in DTO
            branchService.createBranch(branchDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Branch created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Adding New Branch: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long companyId, @PathVariable Long id, @RequestBody BranchDTO branchDTO) {
        try {
            companyService.getById(companyId); // Ensure company exists
            branchDTO.setId(id);
            branchDTO.setCompany(companyService.getById(companyId)); // Set company in DTO
            branchService.updateBranch(branchDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Branch updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Editing Branch: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long companyId, @PathVariable Long id) {
        try {
            branchService.deleteBranch(id, companyId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Branch Deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Deleting Branch: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/delete/batch")
    public ResponseEntity<?> deleteSelectedBranches(@PathVariable Long companyId, @RequestBody List<Long> ids) {
        try {
            branchService.deleteSelected(ids, companyId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Selected branches deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error Deleting Branches: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

/*  @GetMapping("/list")
    public ResponseEntity<List<Branch>> getAllBranches(@PathVariable Long companyId) {
        List<Branch> branches = branchService.listAllByCompanyId(companyId);
        return new ResponseEntity<>(branches, HttpStatus.OK);
        //return ResponseEntity.ok(branches);
    }*/

 /*@DeleteMapping("/delete/batch")
    public ResponseEntity<?> deleteSelectedBranches(@PathVariable Long companyId, @RequestBody List<Long> ids) {
        Map<String, String> response = new HashMap<>();
        try {
            branchService.deleteSelected(ids, companyId);
            response.put("message", "Selected branches deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error Deleting Branch" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }*/
