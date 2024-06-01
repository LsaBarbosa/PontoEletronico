package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.role.EmployeeRole;
import com.santanna.pontoeletronico.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
    public static final UUID ID = UUID.randomUUID();
    public static final String EMPLOYEE_NAME = "Colaborador";
    private static final Integer INDEX = 0;
    public static final String PASSWORD = "123";
    public static final EmployeeRole EMPLOYEE_ROLE = EmployeeRole.ADMIN;
    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ModelMapper mapper;

    private Employee employee;
    private EmployeeDto employeeDto;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startEmployee();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    }

    @Test
    void whenFindAllThenReturnAListOfUserDto() {

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));
        when(mapper.map(any(), any())).thenReturn(employeeDto);

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(EmployeeDto.class, response.getBody().get(INDEX).getClass());

        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(EMPLOYEE_NAME, response.getBody().get(INDEX).getName());

    }

    @Test
    void whenFindByIdThenReturnSuccess()  {
        when(employeeService.getEmployeeById(UUID.randomUUID())).thenReturn((employee));
        when(mapper.map(any(), any())).thenReturn(employeeDto);
        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getName());
        assertEquals(EmployeeDto.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId());
        assertEquals(EMPLOYEE_NAME, response.getBody().getName());

    }



    @Test
    void whenUpdateThenReturnSuccess() {
        // Mock the behavior of employeeService.updateEmployee
        when(employeeService.updateEmployee(anyString(), any())).thenReturn(employee);

        // Mock the behavior of mapper.map
        when(mapper.map(any(), eq(EmployeeDto.class))).thenReturn(employeeDto);

        // Call the update method of the controller
        ResponseEntity<EmployeeDto> response = employeeController.update(EMPLOYEE_NAME, registerDTO);

        // Assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EmployeeDto.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId());
        assertEquals(EMPLOYEE_NAME, response.getBody().getName());

    }

    @Test
    void deleteWithSuccess() {
        doNothing().when(employeeService).deleteEmployee(anyString());

        ResponseEntity<EmployeeDto> response = employeeController.delete(EMPLOYEE_NAME);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(anyString());
    }


    private void startEmployee() {
        employee = new Employee(ID, EMPLOYEE_NAME, PASSWORD, EMPLOYEE_ROLE, null);
        employeeDto = new EmployeeDto(ID, EMPLOYEE_NAME, PASSWORD, EMPLOYEE_ROLE);
        registerDTO = new RegisterDTO(EMPLOYEE_NAME, PASSWORD, EMPLOYEE_ROLE);
    }
}