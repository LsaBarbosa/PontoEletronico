package com.santanna.pontoeletronico.domain.dto;


import com.santanna.pontoeletronico.domain.role.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {
    private UUID id;
    private String name;
    private String password;
    private EmployeeRole role;


}
