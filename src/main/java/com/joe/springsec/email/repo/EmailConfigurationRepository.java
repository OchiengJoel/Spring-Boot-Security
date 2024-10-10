package com.joe.springsec.email.repo;

import com.joe.springsec.company.model.Company;
import com.joe.springsec.email.models.EmailConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, Long> {

    Optional<EmailConfiguration> findByCompany(Company company);
}
