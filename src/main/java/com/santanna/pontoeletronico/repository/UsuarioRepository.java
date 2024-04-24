package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByName(String name);
}
