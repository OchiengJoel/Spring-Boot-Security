package com.joe.springsec.company.repo;


import com.joe.springsec.company.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Long> {

    List<Branch> findAllByCompanyId(Long companyId);
    boolean existsByBranchName(String branchName);
    boolean existsByBranchCode(String branchCode);
    Optional<Branch> findByIdAndCompanyId(Long id, Long companyId);
}
