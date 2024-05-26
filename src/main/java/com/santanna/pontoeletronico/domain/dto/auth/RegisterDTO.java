package com.santanna.pontoeletronico.domain.dto.auth;


import com.santanna.pontoeletronico.domain.entity.EmployeeRole;

public record RegisterDTO(String name, String password, EmployeeRole role) { }


