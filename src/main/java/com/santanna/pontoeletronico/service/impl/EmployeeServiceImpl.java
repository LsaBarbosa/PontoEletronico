package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.service.EmployeeService;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import com.santanna.pontoeletronico.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    public static final String COLABORADOR_NAO_ENCONTRADO = "Colaborador não encontrado";
    public static final String USUARIO_EXISTENTE = "Já existe um colaborador com este nome.";


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(COLABORADOR_NAO_ENCONTRADO));
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getByName(String name) {
        Employee employee = repository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(COLABORADOR_NAO_ENCONTRADO));
        return employee;
    }

    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        findByName(employeeDto);
        return repository.save(modelMapper.map(employeeDto, Employee.class));
    }

    @Override
    public Employee updateEmployee(EmployeeDto employeeDto) {
        findByName(employeeDto);
        getEmployeeById(employeeDto.getId());
        return repository.save(modelMapper.map(employeeDto, Employee.class));
    }


    @Override
    public void deleteEmployee(Long id) {
        getEmployeeById(id);
        repository.deleteById(id);
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void findByName(EmployeeDto employeeDto) {
        Optional<Employee> employee = repository.findByName(employeeDto.getName());
        if (employee.isPresent() && employee.get().getId().equals(employeeDto.getId())) {
            throw new DataIntegrityViolationException(USUARIO_EXISTENTE);
        }
    }
}
