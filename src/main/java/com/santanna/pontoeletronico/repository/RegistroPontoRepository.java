package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.RegistroPonto;
import com.santanna.pontoeletronico.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    List<RegistroPonto> findByUsuario(Usuario usuario);

    RegistroPonto findTopByUsuarioAndSaidaIsNullOrderByEntradaDesc(Usuario usuario);

    List<RegistroPonto> findByUsuarioAndDataBetween(Usuario usuario, LocalDate dataInicial, LocalDate dataFinal);


    @Query("SELECT rp FROM RegistroPonto rp WHERE rp.usuario.id = :userId AND rp.saida IS NULL")
    RegistroPonto findRegistroEntradaAtivo(@Param("userId") Long userId);
}



