package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistroPontoUsuarioDto {
    private String nome;
    private List<RegistroPontoDetalhadoDto> registros;
}
