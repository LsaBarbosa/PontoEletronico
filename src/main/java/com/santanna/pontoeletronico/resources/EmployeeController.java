package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        var getId = employeeService.getEmployeeById(id);
        return ResponseEntity.ok().body(modelMapper.map(getId, EmployeeDto.class));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        var getAll = employeeService.getAllEmployees();
        return ResponseEntity.ok(getAll.stream().map(x -> modelMapper.map(x, EmployeeDto.class)).collect(Collectors.toList()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        employeeDto.setId(id);
        Employee employee = employeeService.updateEmployee(employeeDto);
        return ResponseEntity.ok().body(modelMapper.map(employee, EmployeeDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDto> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
