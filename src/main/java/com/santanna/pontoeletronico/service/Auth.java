package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.AuthenticationDTO;
import com.santanna.pontoeletronico.domain.dto.auth.LoginResponseDTO;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.security.TokenService;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Auth {

    public static final String EMPLOYEE_ALREADY_EXIST = "Já existe um colaborador com este nome.";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ModelMapper modelMapper;

    public LoginResponseDTO login(AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.name(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Employee) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    public RegisterDTO register(RegisterDTO registerDTO){
        if (this.repository.findByName(registerDTO.name()) != null)
      throw new DataIntegrityViolationException(EMPLOYEE_ALREADY_EXIST);
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        Employee newEmployee = new Employee(registerDTO.name(), encryptedPassword, registerDTO.role() );
        var save =this.repository.save(newEmployee);
        EmployeeDto dto = modelMapper.map(save, EmployeeDto.class);
        return new RegisterDTO(dto.getName(), encryptedPassword, dto.getRole());
    }
}
