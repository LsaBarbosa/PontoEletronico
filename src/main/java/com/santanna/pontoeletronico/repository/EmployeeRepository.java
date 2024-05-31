package com.santanna.pontoeletronico.repository;


import com.santanna.pontoeletronico.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByNameContainsIgnoreCase(String name);
    UserDetails findByName(String name);
}
