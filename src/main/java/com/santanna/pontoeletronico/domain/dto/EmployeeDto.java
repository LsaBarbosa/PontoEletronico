package com.santanna.pontoeletronico.domain.dto;


import com.santanna.pontoeletronico.domain.entity.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {
    private Long id;
    private String name;
private String password;
private EmployeeRole role;



}
