package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.RecordDto;
import com.santanna.pontoeletronico.domain.dto.EmployeeRegisterDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.EmployeeService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRegisterDto> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        EmployeeRegisterDto usuarioDto = new EmployeeRegisterDto();
        usuarioDto.setId(employee.getId());
        usuarioDto.setName(employee.getName());
        List<RecordDto> recordDto = employee.getWorkTime().stream()
                .map(registro -> new RecordDto(registro.getId(), registro.getStartOfWork(), registro.getEndOfWork(),registro.getTimeWorkedInMinutes()))
                .collect(Collectors.toList());
        usuarioDto.setTimeRecording(recordDto);
        return ResponseEntity.ok(usuarioDto);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> employees = employeeService.getAll();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.create(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.update(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
