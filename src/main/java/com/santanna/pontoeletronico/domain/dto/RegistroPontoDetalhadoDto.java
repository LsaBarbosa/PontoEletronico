package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPontoDetalhadoDto {
    private Long id;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private long MinutosTrabalhados;
    private long horasExtras;
}
