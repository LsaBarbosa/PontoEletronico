package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioRegisterDto {

    private Long id;
    private String name;
    private List<RegistroDto> registrosPonto;

}
