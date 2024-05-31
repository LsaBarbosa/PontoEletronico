package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;

import java.util.List;

public interface EmployeeService {
Employee getEmployeeById(Long id);
List<Employee> getAllEmployees();
Employee getByName(String name);
Employee createEmployee(EmployeeDto employeeDto);
Employee updateEmployee(String name, RegisterDTO registerDTO);
void deleteEmployee(String name);
}
