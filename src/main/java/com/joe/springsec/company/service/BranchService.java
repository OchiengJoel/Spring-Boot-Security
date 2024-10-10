package com.joe.springsec.company.service;

import com.joe.springsec.company.dto.BranchDTO;
import com.joe.springsec.company.mapper.BranchMapper;
import com.joe.springsec.company.model.Branch;
import com.joe.springsec.company.repo.BranchRepo;
import com.joe.springsec.company.repo.CompanyRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchService {

    private static final String CODE_PREFIX = "BRC";
    private final BranchRepo branchRepo;
    private final CompanyRepo companyRepo;
    private final BranchMapper branchMapper;

    public BranchService(BranchRepo branchRepo, CompanyRepo companyRepo, BranchMapper branchMapper) {
        this.branchRepo = branchRepo;
        this.companyRepo = companyRepo;
        this.branchMapper = branchMapper;
    }

    public List<BranchDTO> listAllByCompanyId(Long companyId) {
        List<Branch> branches = branchRepo.findAllByCompanyId(companyId);
        return branches.stream()
                .map(branchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBranch(BranchDTO branchDTO) {
        if (branchRepo.existsByBranchName(branchDTO.getBranchName())) {
            throw new IllegalArgumentException("Branch Name Already Exists");
        }
        if (branchRepo.existsByBranchCode(branchDTO.getBranchCode())) {
            throw new IllegalArgumentException("Branch Code Already Exists");
        }

        Branch branch = branchMapper.toEntity(branchDTO);
        branch.setBranchCode(generateUniqueCode());
        branchRepo.save(branch);
    }

    @Transactional
    public void updateBranch(BranchDTO branchDTO) {
        Branch branch = branchMapper.toEntity(branchDTO);
        if (!branchRepo.existsById(branch.getId())) {
            throw new IllegalArgumentException("Branch Does Not Exist");
        }
        branchRepo.save(branch);
    }

    @Transactional
    public void deleteBranch(Long id, Long companyId) {
        Branch branch = findByIdAndCompanyId(id, companyId);
        branchRepo.delete(branch);
    }

    @Transactional
    public void deleteSelected(List<Long> ids, Long companyId) {
        for (Long id : ids) {
            deleteBranch(id, companyId);
        }
    }

    public Branch findByIdAndCompanyId(Long id, Long companyId) {
        return branchRepo.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Branch Not Found for the specified company"));
    }

    public BranchDTO toDTO(Branch branch) {
        return branchMapper.toDTO(branch);
    }

    private synchronized String generateUniqueCode() {
        long timestamp = System.currentTimeMillis();
        int randomComponent = (int) (Math.random() * 10000);
        return CODE_PREFIX + "-" + timestamp + "-" + String.format("%04d", randomComponent);
    }
}
