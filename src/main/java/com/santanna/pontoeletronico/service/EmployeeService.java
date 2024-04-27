package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository repository;

    public Employee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee create(Employee employee) {

        if (repository.existsByName(employee.getName())) {
            throw new RuntimeException("Já existe um usuário com este nome.");
        }
        return repository.save(employee);
    }

    public Employee update(Long id, Employee employee) {
        Employee existingEmployee = getById(id);
        existingEmployee.setName(employee.getName());

        return repository.save(existingEmployee);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
