package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RecordWorkTime, Long> {

    List<RecordWorkTime> findByColaborador(Employee employee);

    RecordWorkTime findTopByColaboradorAndEndOfWorkIsNullOrderByStartOfWorkDesc(Employee employee);

    List<RecordWorkTime> findByColaboradorAndDateBetween(Employee employee, LocalDate dataInicial, LocalDate dataFinal);


    @Query("SELECT rp FROM RecordWorkTime rp WHERE rp.colaborador.id = :userId AND rp.endOfWork IS NULL")
    RecordWorkTime findRegistroEntradaAtivo(@Param("userId") Long userId);
}



