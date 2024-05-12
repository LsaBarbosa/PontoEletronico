package com.santanna.pontoeletronico.repository;


import com.santanna.pontoeletronico.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByName(String name);
    Optional<Employee> findByName(String name);
}
