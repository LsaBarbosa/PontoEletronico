package com.santanna.pontoeletronico.repository;


import com.santanna.pontoeletronico.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByNameContainsIgnoreCase(String name);
    UserDetails findByName(String name);
}
