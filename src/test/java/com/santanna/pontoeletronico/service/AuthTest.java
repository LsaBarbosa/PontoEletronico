package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.AuthenticationDTO;
import com.santanna.pontoeletronico.domain.dto.auth.LoginResponseDTO;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.EmployeeRole;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.security.TokenService;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthTest {
    public static final String NAME = "testUser";
    public static final String PASSWORD = "testPassword";
    public static final EmployeeRole EMPLOYEE_ROLE_ADMIN = EmployeeRole.ADMIN;
    public static final EmployeeRole EMPLOYEE_ROLE_USER = EmployeeRole.USER;
    public static final String TOKEN = "testToken";
    public static final String ACCESS_DENIED = "Usuário não autorizado para cadastro.";
    @InjectMocks
    private Auth authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmployeeRepository repository;

    @Mock
    private TokenService tokenService;

    @Mock
    private ModelMapper modelMapper;

    private AuthenticationDTO authenticationDTO;
    private RegisterDTO registerDTO;
    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationDTO = new AuthenticationDTO(NAME, PASSWORD);
        registerDTO = new RegisterDTO(NAME, PASSWORD, EMPLOYEE_ROLE_ADMIN);
        employee = new Employee(NAME, new BCryptPasswordEncoder().encode(PASSWORD), EMPLOYEE_ROLE_USER);
        employeeDto = new EmployeeDto(UUID.randomUUID(), NAME, PASSWORD, EMPLOYEE_ROLE_ADMIN); // Alteração para UUID
    }

    @Test
    public void testLoginSuccess() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(employee, employee.getPassword()));
        when(tokenService.generateToken(any(Employee.class))).thenReturn(TOKEN);

        LoginResponseDTO response = authService.login(authenticationDTO);

        assertNotNull(response);
        assertEquals(TOKEN, response.token());
    }

    @Test
    public void testRegisterSuccess() {
        when(repository.findByName(registerDTO.name())).thenReturn(null);
        when(repository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), eq(EmployeeDto.class))).thenReturn(employeeDto);

        RegisterDTO response = authService.register(registerDTO);

        assertNotNull(response);
        assertEquals(registerDTO.name(), response.name());
        assertEquals(registerDTO.role(), response.role());
    }

    @Test
    public void testRegisterEmployeeAlreadyExists() {
        when(repository.findByName(registerDTO.name())).thenReturn(employee);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            authService.register(registerDTO);
        });

        assertEquals(Auth.ACCESS_DENIED, exception.getMessage());
    }
}
