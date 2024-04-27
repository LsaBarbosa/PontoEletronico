package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
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
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getById(id);
        return ResponseEntity.ok(employeeDto);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
//        Employee employee = employeeService.getById(id);
//        EmployeeDto usuarioDto = new EmployeeDto();
//        usuarioDto.setId(employee.getId());
//        usuarioDto.setName(employee.getName());
//        List<RecordCheckinDto> recordDto = employee.getWorkTime().stream()
//                .map(registro -> new RecordCheckinDto(registro))
//                .collect(Collectors.toList());
//        usuarioDto.setTimeRecording(recordDto);
//        return ResponseEntity.ok(usuarioDto);
//    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<EmployeeDto> employees = employeeService.getAll();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.create(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.update(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
