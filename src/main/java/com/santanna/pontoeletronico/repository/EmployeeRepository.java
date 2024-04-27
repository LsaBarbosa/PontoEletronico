package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Employee, Long> {

    boolean existsByName(String name);
}
