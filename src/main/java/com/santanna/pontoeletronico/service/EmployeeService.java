package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.utils.EmployeeMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.santanna.pontoeletronico.utils.EmployeeMapperUtils.mapToDto;

@Service
public class EmployeeService{


    @Autowired
    private EmployeeRepository repository;

    public EmployeeDto getById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return mapToDto(employee);
    }


    public List<EmployeeDto> getAll() {
        List<Employee> employees = repository.findAll();
        return employees.stream()
                .map(EmployeeMapperUtils::mapToDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto create(EmployeeDto employeeDto) {
        if (repository.existsByName(employeeDto.getName())) {
            throw new RuntimeException("Já existe um usuário com este nome.");
        }
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        Employee savedEmployee = repository.save(employee);
        return mapToDto(savedEmployee);
    }

    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        existingEmployee.setName(employeeDto.getName());
        Employee updatedEmployee = repository.save(existingEmployee);
        return mapToDto(updatedEmployee);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
