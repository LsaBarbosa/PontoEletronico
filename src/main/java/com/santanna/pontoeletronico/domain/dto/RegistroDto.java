package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistroDto {
    private Long id;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private Long minutosTrabalhadas;
}
