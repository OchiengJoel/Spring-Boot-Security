package com.joe.springsec.company.repo;


import com.joe.springsec.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

    boolean existsByCompanyName(String companyName);
}
