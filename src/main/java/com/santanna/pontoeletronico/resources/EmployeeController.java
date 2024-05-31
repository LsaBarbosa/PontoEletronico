package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable UUID id) {
        var getId = employeeService.getEmployeeById(id);
        return ResponseEntity.ok().body(modelMapper.map(getId, EmployeeDto.class));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        var getAll = employeeService.getAllEmployees();
        return ResponseEntity.ok(getAll.stream().map(x -> modelMapper.map(x, EmployeeDto.class)).collect(Collectors.toList()));
    }


    @PutMapping()
    public ResponseEntity<EmployeeDto> update(@RequestParam String name, @RequestBody RegisterDTO registerDTO) {
        Employee employee = employeeService.updateEmployee(name, registerDTO);
        return ResponseEntity.ok().body(modelMapper.map(employee, EmployeeDto.class));
    }

    @DeleteMapping()
    public ResponseEntity<EmployeeDto> delete(@RequestParam String name) {
        employeeService.deleteEmployee(name);
        return ResponseEntity.noContent().build();
    }
}
