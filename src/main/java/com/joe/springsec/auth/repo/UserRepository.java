package com.joe.springsec.auth.repo;

import com.joe.springsec.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // Custom query to find users by company
    @Query("SELECT u FROM User u JOIN u.companies c WHERE c.id = :companyId")
    List<User> findUsersByCompanyId(@Param("companyId") Long companyId);
}
