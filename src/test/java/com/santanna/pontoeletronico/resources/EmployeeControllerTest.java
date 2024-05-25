package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
    public static final long ID = 1L;
    public static final String EMPLOYEE = "Colaborador";
     private static final Integer INDEX = 0;
    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ModelMapper mapper;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startEmployee();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    }

    @Test
    void whenFindAllThenReturnAListOfUserDto() {
        // seguindo o fluxo da chamada
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee)); // quando o serviço for chamado retorna o user
        when(mapper.map(any(), any())).thenReturn(employeeDto); // convertendo para o dto

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(EmployeeDto.class, response.getBody().get(INDEX).getClass());

        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(EMPLOYEE, response.getBody().get(INDEX).getName());

    }

    @Test
    void whenFindByIdThenReturnSuccess()  {
        when(employeeService.getEmployeeById(anyLong())).thenReturn((employee));
        when(mapper.map(any(), any())).thenReturn(employeeDto);
        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getName());
        assertEquals(EmployeeDto.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId());
        assertEquals(EMPLOYEE, response.getBody().getName());

    }

    @Test
    void whenCreateThenReturnCreated() {
        when(employeeService.createEmployee(any())).thenReturn(employee);
        ResponseEntity<EmployeeDto> response = employeeController.create(employeeDto);
        assertNotNull(response.getHeaders().get("Location"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(employeeService.updateEmployee(any())).thenReturn(employee);
        when(mapper.map(any(), any())).thenReturn(employeeDto);
        ResponseEntity<EmployeeDto> response = employeeController.update(ID, employeeDto);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(EmployeeDto.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId());
        assertEquals(EMPLOYEE, response.getBody().getName());

    }

    @Test
    void deleteWithSuccess() {
        doNothing().when(employeeService).deleteEmployee(anyLong());
        ResponseEntity<EmployeeDto> response = employeeController.delete(ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(anyLong());
    }

    private void startEmployee() {
        employee = new Employee(ID, EMPLOYEE, null);
        employeeDto = new EmployeeDto(ID, EMPLOYEE);
    }
}