package com.santanna.pontoeletronico.domain.dto;

import com.santanna.pontoeletronico.domain.entity.RegistroPonto;

import java.time.LocalDateTime;

public record RegistroPontoDto(Long id, LocalDateTime entrada, LocalDateTime saida, Long minutosTrabalhadas) {

    public RegistroPontoDto(RegistroPonto registro) {
        this(registro.getId(), registro.getEntrada(), registro.getSaida(), registro.getMinutosTrabalhadas());

    }
}
